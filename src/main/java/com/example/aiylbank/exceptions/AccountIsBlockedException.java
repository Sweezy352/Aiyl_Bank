package com.example.aiylbank.exceptions;

import org.springframework.http.HttpStatus;

public class AccountIsBlockedException extends BaseException {
    public AccountIsBlockedException(String message) {
        super(ErrorResponse.builder().message(message).status(400).build());
    }
}
