package com.example.cloud_back.dto;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class LoginDto {

    @Size(min = 4)
    private String login;

    @Size(min = 3)
    private String password;
}
