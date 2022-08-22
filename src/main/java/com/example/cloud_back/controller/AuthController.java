package com.example.cloud_back.controller;

import com.example.cloud_back.dto.AuthDto;
import com.example.cloud_back.dto.LoginDto;
import com.example.cloud_back.exception_handling.UnauthorizedException;
import com.example.cloud_back.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/")
@Validated
@RequiredArgsConstructor
public class AuthController {

    private final SecurityService securityService;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthDto> login(@Valid @RequestBody LoginDto loginDto) {
        Authentication authentication = securityService.attemptAuthentication(loginDto);
        if (!authentication.isAuthenticated()) {
            throw new UnauthorizedException();
        }
        return  ResponseEntity.ok(securityService.successfulAuthentication(authentication));
    }

    @PostMapping(value = "/logout")
    public HttpStatus logout(@RequestHeader("auth-token") String authToken) {
        securityService.logout(authToken);
        return HttpStatus.OK;
    }
}
