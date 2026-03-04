package com.example.aiylbank.exceptions;

import java.time.LocalDateTime;

public class AccountNotFoundException extends BaseException {
    public AccountNotFoundException(String message) {
        super(ErrorResponse.builder().message(message).status(404).build());
    }
}
