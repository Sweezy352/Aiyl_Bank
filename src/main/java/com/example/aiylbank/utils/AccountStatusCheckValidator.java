package com.example.aiylbank.utils;

import com.example.aiylbank.entity.Account;
import com.example.aiylbank.enums.AccountStatus;
import com.example.aiylbank.exceptions.AccountIsBlockedException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountStatusCheckValidator implements TransferValidator{
    @Override
    public void validate(Account sender, Account receiver, BigDecimal amount) {
        if (sender.getStatus().equals(AccountStatus.BLOCKED))
            throw new AccountIsBlockedException("Sender account is blocked");
        if (receiver.getStatus().equals(AccountStatus.BLOCKED))
            throw new AccountIsBlockedException("Receiver account is blocked");
    }
}
