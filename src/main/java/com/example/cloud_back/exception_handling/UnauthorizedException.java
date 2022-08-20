package com.example.cloud_back.exception_handling;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        super("Unauthorized error");
    }
}
