package com.example.aiylbank.exceptions;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private ErrorResponse errorBody;

    public BaseException(ErrorResponse errorBody) {
        super(errorBody.getMessage());
        this.errorBody = errorBody;
    }
}
