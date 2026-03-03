package com.example.aiylbank.services.impl;

import com.example.aiylbank.annotations.Loggable;
import com.example.aiylbank.entity.Account;
import com.example.aiylbank.enums.AccountStatus;
import com.example.aiylbank.exceptions.AccountNotFoundException;
import com.example.aiylbank.repository.AccountRepository;
import com.example.aiylbank.services.AccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Override
    @Loggable
    @Transactional
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new AccountNotFoundException("Account not found"));
    }

    @Override
    @Loggable
    public Account updateStatus(String accountNumber, String status) {
        Account account = getAccountByAccountNumber(accountNumber);
        account.setStatus(AccountStatus.valueOf(status));
        return accountRepository.save(account);
    }
}
