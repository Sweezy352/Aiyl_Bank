package com.example.aiylbank.utils;

import com.example.aiylbank.entity.Account;
import com.example.aiylbank.exceptions.SameAccountNumberException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SameAccountValidator implements TransferValidator{
    @Override
    public void validate(Account sender, Account receiver, BigDecimal amount) {
        if(sender.getAccountNumber().equals(receiver.getAccountNumber())) throw new SameAccountNumberException("Sender and receiver cannot be the same account");
    }
}
