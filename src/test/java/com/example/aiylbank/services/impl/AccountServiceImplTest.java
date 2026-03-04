package com.example.aiylbank.services.impl;

import com.example.aiylbank.entity.Account;
import com.example.aiylbank.enums.AccountStatus;
import com.example.aiylbank.exceptions.AccountNotFoundException;
import com.example.aiylbank.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account testAccount;

    @BeforeEach
    void setUp() {
        testAccount = new Account();
        testAccount.setId(1L);
        testAccount.setAccountNumber("1234567890");
        testAccount.setBalance(BigDecimal.valueOf(1000.00));
        testAccount.setStatus(AccountStatus.ACTIVE);
    }

    @Test
    void createAccount_shouldSaveAndReturnAccount() {
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        Account createdAccount = accountService.createAccount(new Account());

        assertNotNull(createdAccount);
        assertEquals("1234567890", createdAccount.getAccountNumber());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void getAccountByAccountNumber_shouldReturnAccount_whenFound() {
        when(accountRepository.findByAccountNumber("1234567890")).thenReturn(Optional.of(testAccount));

        Account foundAccount = accountService.getAccountByAccountNumber("1234567890");

        assertNotNull(foundAccount);
        assertEquals("1234567890", foundAccount.getAccountNumber());
        verify(accountRepository, times(1)).findByAccountNumber("1234567890");
    }

    @Test
    void getAccountByAccountNumber_shouldThrowAccountNotFoundException_whenNotFound() {
        when(accountRepository.findByAccountNumber("nonExistent")).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountByAccountNumber("nonExistent"));
        verify(accountRepository, times(1)).findByAccountNumber("nonExistent");
    }

    @Test
    void updateStatus_shouldUpdateAndReturnAccount() {
        Account updatedAccount = new Account();
        updatedAccount.setId(1L);
        updatedAccount.setAccountNumber("1234567890");
        updatedAccount.setBalance(BigDecimal.valueOf(1000.00));
        updatedAccount.setStatus(AccountStatus.BLOCKED);

        when(accountRepository.findByAccountNumber("1234567890")).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(updatedAccount);

        Account result = accountService.updateStatus("1234567890", "BLOCKED");

        assertNotNull(result);
        assertEquals(AccountStatus.BLOCKED, result.getStatus());
        verify(accountRepository, times(1)).findByAccountNumber("1234567890");
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void updateStatus_shouldThrowAccountNotFoundException_whenAccountNotFound() {
        when(accountRepository.findByAccountNumber("nonExistent")).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.updateStatus("nonExistent", "BLOCKED"));
        verify(accountRepository, times(1)).findByAccountNumber("nonExistent");
        verify(accountRepository, never()).save(any(Account.class));
    }
}
