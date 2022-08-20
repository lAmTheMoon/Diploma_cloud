package com.example.cloud_back.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.cloud_back.authservice.Tokens;
import com.example.cloud_back.dto.AuthDto;
import com.example.cloud_back.dto.LoginDto;
import com.example.cloud_back.exception_handling.CredentialException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SecurityService {
    @Value("${jwt.token.secret}")
    private String secret;

    private final Tokens tokens;
    private final AuthenticationManager authenticationManager;

    public Authentication attemptAuthentication(LoginDto loginDto) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getLogin(), loginDto.getPassword());
        log.info("Username {}, token {}", loginDto.getLogin(), authenticationToken);
        return Optional.ofNullable(authenticationManager.authenticate(authenticationToken)).orElseThrow(
                CredentialException::new);
    }

    public AuthDto successfulAuthentication(Authentication authResult) {
        User user = (User) authResult.getPrincipal();
        String token = createToken(user, Algorithm.HMAC256(secret));
        tokens.add(token);
        log.info("Sending access_token: {}", token);
        System.out.println(token);
        return new AuthDto(token);
    }

    private String createToken(User user, Algorithm algorithm) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("authorities", user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .sign(algorithm);
    }
}
