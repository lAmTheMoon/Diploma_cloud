package com.example.cloud_back.exception_handling;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CloudException extends RuntimeException {

    private String info;
}
