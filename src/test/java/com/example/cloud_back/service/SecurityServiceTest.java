package com.example.cloud_back.service;

import com.example.cloud_back.authservice.Tokens;
import com.example.cloud_back.dto.LoginDto;
import com.example.cloud_back.exception_handling.CredentialException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class SecurityServiceTest {

    @Mock
    private Tokens tokens;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private SecurityService securityService;

    @Test
    void attemptAuthenticationExceptionTest() {
        LoginDto loginDto = new LoginDto();
        loginDto.setLogin("login");
        loginDto.setPassword("password");

        Exception exception = assertThrows(CredentialException.class, () -> {
            securityService.attemptAuthentication(loginDto);
        });

        String expectedMessage = "Bad credentials";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}