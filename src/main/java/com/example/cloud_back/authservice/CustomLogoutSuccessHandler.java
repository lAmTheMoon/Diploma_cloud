package com.example.cloud_back.authservice;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {

    @Value("${jwt.token.header}")
    private String header;
    @Value("${jwt.token.start}")
    private String start;

    private final Tokens tokens;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) {
        Optional<String> authHeader = Optional.ofNullable(request.getHeader(header));
        if (authHeader.isPresent() && authHeader.get().startsWith(start)) {
            String token = authHeader.get().substring(start.length());
            tokens.remove(token);
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }
}