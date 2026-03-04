package com.example.aiylbank.utils;

import com.example.aiylbank.entity.Account;

import java.math.BigDecimal;

public interface TransferValidator {
    void validate(Account sender, Account receiver, BigDecimal amount);
}
