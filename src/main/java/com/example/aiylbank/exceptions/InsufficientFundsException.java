package com.example.aiylbank.exceptions;

public class InsufficientFundsException extends BaseException {
    public InsufficientFundsException(String message) {
        super(ErrorResponse.builder().message(message).status(400).build());
    }
}
