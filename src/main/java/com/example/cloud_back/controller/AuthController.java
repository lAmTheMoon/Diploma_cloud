package com.example.cloud_back.controller;

import com.example.cloud_back.config.PasswordEncoderConfig;
import com.example.cloud_back.dto.AuthDto;
import com.example.cloud_back.dto.LoginDto;
import com.example.cloud_back.exception_handling.UnauthorizedException;
import com.example.cloud_back.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@Slf4j
@CrossOrigin
@RequiredArgsConstructor
public class AuthController {

    private final SecurityService securityService;
    private final PasswordEncoderConfig passwordEncoder;

    @ResponseBody @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthDto> login(@Valid @RequestBody LoginDto loginDto) {
        Authentication authentication = securityService.attemptAuthentication(loginDto);
        if (!authentication.isAuthenticated()) {
            throw new UnauthorizedException();
        }
        return  ResponseEntity.ok(securityService.successfulAuthentication(authentication));
    }

    @PostMapping(value ="/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ResponseBody @PostMapping(value = "/encode", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String encode(@RequestBody String password) {
        PasswordEncoder passwordEnc = passwordEncoder.passwordEncoder();
        return passwordEnc.encode(password);
    }
}
