package com.example.cloud_back.exception_handling;

public class CredentialException extends RuntimeException {

    public CredentialException() {
        super("Bad credentials");
    }
}
