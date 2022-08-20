package com.example.cloud_back.authservice;

import lombok.Data;

@Data
public class AuthorityFail {
    private String message;
    private int id;
    private static int idStatic;

    public AuthorityFail() {
        idStatic++;
        id = idStatic;
    }

    public AuthorityFail(String message) {
        this.message = message;
        idStatic++;
        id = idStatic;
    }
}
