package com.example.cloud_back.service;

import com.example.cloud_back.authservice.CustomUtils;
import com.example.cloud_back.dto.AuthDto;
import com.example.cloud_back.dto.LoginDto;
import com.example.cloud_back.exception_handling.CloudException;
import com.example.cloud_back.exception_handling.CredentialException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SecurityService {

    private final AuthenticationManager authenticationManager;
    private final CustomUtils customUtils;
    private final StorageService service;

    public Authentication attemptAuthentication(LoginDto loginDto) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getLogin(), loginDto.getPassword());
        log.info("Username {}, token {}", loginDto.getLogin(), authenticationToken);
        return Optional.ofNullable(authenticationManager.authenticate(authenticationToken)).orElseThrow(
                CredentialException::new);
    }

    public AuthDto successfulAuthentication(Authentication authResult) {
        try {
            SecurityContextHolder.getContext().setAuthentication(authResult);
            UserDetails userPrincipal = (UserDetails) authResult.getPrincipal();
            String jwt = customUtils.generateJwtToken(userPrincipal);
            service.login(jwt, userPrincipal);
            log.info(String.format("Authorization success. %s : %s", userPrincipal.getUsername(), jwt));
            return AuthDto.builder()
                    .token(jwt).build();
        } catch (BadCredentialsException e) {
            throw new CredentialException();
        }
    }

    public void logout(String authToken) {
        UserDetails userPrincipal = service.logout(authToken).orElseThrow(CloudException::new);
        log.info(String.format("User %s logout success", userPrincipal.getUsername()));
    }
}
