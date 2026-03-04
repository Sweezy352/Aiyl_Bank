package com.example.aiylbank.exceptions;

public class SameAccountNumberException extends BaseException {
    public SameAccountNumberException(String message) {
        super(ErrorResponse.builder().message(message).status(400).build());
    }
}
