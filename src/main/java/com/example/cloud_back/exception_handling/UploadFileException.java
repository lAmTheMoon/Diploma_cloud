package com.example.cloud_back.exception_handling;

public class UploadFileException extends RuntimeException {

    public UploadFileException() {
        super("Error upload file");
    }
}
