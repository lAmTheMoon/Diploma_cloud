package com.example.cloud_back.exception_handling;

public class DeleteFileException extends RuntimeException {

    public DeleteFileException() {
        super("Error delete file");
    }
}
