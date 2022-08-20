package com.example.cloud_back.authservice;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.cloud_back.exception_handling.NotFoundTokenException;
import com.example.cloud_back.model.Authority;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.token.header}")
    private String header;
    @Value("${jwt.token.secret}")
    private String secret;
    @Value("${jwt.token.start}")
    private String start;

    private final Tokens tokens;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (!"/login".equals(request.getServletPath())) {
            Optional<String> authHeader = Optional.ofNullable(request.getHeader(header));

            if (authHeader.isPresent() && authHeader.get().startsWith(start)) {
                try {
                    String token = authHeader.get().substring(start.length());
                    if (!tokens.contains(token)) {
                        throw new NotFoundTokenException();
                    }

                    SecurityContextHolder.getContext().setAuthentication(getAuthenticationToken(token));

                } catch (Exception e) {
                    log.error("Error login in: {}", e.getMessage());
                    response.setStatus(UNAUTHORIZED.value());
                    AuthorityFail authorityFail = new AuthorityFail(e.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), authorityFail);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    @NotNull
    private UsernamePasswordAuthenticationToken getAuthenticationToken(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
        String username = decodedJWT.getSubject();
        Collection<Authority> authorities = getAuthorities(decodedJWT);
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

    @NotNull
    private List<Authority> getAuthorities(DecodedJWT decodedJWT) {
        return decodedJWT.getClaim("authorities").asList(String.class).stream()
                .map(Authority::new)
                .collect(Collectors.toList());
    }
}
