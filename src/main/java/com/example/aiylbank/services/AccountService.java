package com.example.aiylbank.services;

import com.example.aiylbank.entity.Account;

public interface AccountService {
    Account createAccount(Account account);
    Account getAccountByAccountNumber(String accountNumber);
    Account updateStatus(String accountNumber, String status);
}
