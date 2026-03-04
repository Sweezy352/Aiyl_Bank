package com.example.aiylbank.utils;

import com.example.aiylbank.entity.Account;
import com.example.aiylbank.exceptions.InsufficientFundsException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component

public class BalanceValidator implements TransferValidator{
    @Override
    public void validate(Account sender, Account receiver, BigDecimal amount) {
        if (sender.getBalance().compareTo(amount) < 0) throw new InsufficientFundsException("Not enough money");
    }
}
