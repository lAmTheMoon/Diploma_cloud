package com.example.cloud_back.exception_handling;

public class IncorrectDataException extends RuntimeException {

    public IncorrectDataException() {
        super("Error input data");
    }
}