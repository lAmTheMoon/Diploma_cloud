package com.example.cloud_back.authservice;

import com.example.cloud_back.exception_handling.IncorrectDataException;
import com.example.cloud_back.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

    private final CustomUtils customUtils;
    private final UserServiceImpl userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwt = parseJwt(request);
            if (jwt != null && customUtils.validateJwtToken(jwt)) {
                String login = customUtils.getUserNameFromJwtToken(jwt);

                UserDetails userPrincipal = userService.loadUserByUsername(login);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userPrincipal, null, userPrincipal.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (IncorrectDataException exc) {
            log.error("Error ExpiredJwtException: JWT has been expired");
        } catch (Exception exc) {
            log.error(String.format("Error %s while getting JWT: %s", exc.getClass().getName(), exc.getLocalizedMessage()));
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader(header);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(start)) {
            return headerAuth.substring(6);
        } else {
            log.error("Error Invalid auth-token format: should start with 'Bearer'");
        }
        return null;
    }
}
