package com.example.cloud_back.exception_handling;

public class NotFoundTokenException extends RuntimeException {

    public NotFoundTokenException() {
        super("Token not found");
    }
}
