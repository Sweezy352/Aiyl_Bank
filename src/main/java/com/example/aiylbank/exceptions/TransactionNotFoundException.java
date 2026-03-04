package com.example.aiylbank.exceptions;

public class TransactionNotFoundException extends BaseException {
    public TransactionNotFoundException(String message) {
        super(ErrorResponse.builder().message(message).status(404).build());
    }
}
