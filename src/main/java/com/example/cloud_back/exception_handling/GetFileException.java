package com.example.cloud_back.exception_handling;

public class GetFileException extends RuntimeException {

    public GetFileException() {
        super("Error getting file list");
    }
}
