package com.example.aiylbank.services.impl;

import com.example.aiylbank.dto.request.TransactionDtoRequest;
import com.example.aiylbank.entity.Account;
import com.example.aiylbank.entity.TransactionEntity;
import com.example.aiylbank.enums.AccountStatus;
import com.example.aiylbank.enums.OperationType;
import com.example.aiylbank.exceptions.AccountNotFoundException;
import com.example.aiylbank.repository.AccountRepository;
import com.example.aiylbank.services.TransactionService;
import com.example.aiylbank.utils.TransferValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionService transactionService;

    @Mock
    private TransferValidator transferValidator;

    @InjectMocks
    private TransferServiceImpl transferService;

    private Account senderAccount;
    private Account receiverAccount;
    private TransactionDtoRequest transactionDtoRequest;

    @BeforeEach
    void setUp() {
        senderAccount = new Account();
        senderAccount.setId(1L);
        senderAccount.setAccountNumber("1234567890");
        senderAccount.setBalance(BigDecimal.valueOf(1000.00));
        senderAccount.setStatus(AccountStatus.ACTIVE);

        receiverAccount = new Account();
        receiverAccount.setId(2L);
        receiverAccount.setAccountNumber("0987654321");
        receiverAccount.setBalance(BigDecimal.valueOf(500.00));
        receiverAccount.setStatus(AccountStatus.ACTIVE);

        transactionDtoRequest = new TransactionDtoRequest();
        transactionDtoRequest.setSenderAccountNumber("1234567890");
        transactionDtoRequest.setReceiverAccountNumber("0987654321");
        transactionDtoRequest.setAmount(BigDecimal.valueOf(100.00));

        transferService = new TransferServiceImpl(accountRepository, transactionService, Collections.singletonList(transferValidator));
    }

    @Test
    void deposit_shouldIncreaseReceiverBalanceAndCreateSuccessTransaction() {
        when(accountRepository.findByAccountNumber("0987654321")).thenReturn(Optional.of(receiverAccount));
        when(transactionService.createSuccessTransaction(any(), any(), any(), any(), any())).thenReturn(new TransactionEntity());

        transferService.deposit(transactionDtoRequest);

        assertEquals(BigDecimal.valueOf(600.00), receiverAccount.getBalance());
        verify(accountRepository, times(1)).save(receiverAccount);
        verify(transactionService, times(1)).createSuccessTransaction(null, receiverAccount, BigDecimal.valueOf(100.00), BigDecimal.valueOf(600.00), OperationType.DEPOSIT);
    }

    @Test
    void deposit_shouldCreateFailedTransaction_whenExceptionOccurs() {
        when(accountRepository.findByAccountNumber("0987654321")).thenReturn(Optional.of(receiverAccount));
        doThrow(new RuntimeException("Test Exception")).when(accountRepository).save(any(Account.class));

        transferService.deposit(transactionDtoRequest);

        verify(transactionService, times(1)).createFailedTransaction(null, receiverAccount, BigDecimal.valueOf(100.00), BigDecimal.valueOf(600.00), OperationType.DEPOSIT, "Test Exception");
    }

    @Test
    void transfer_shouldUpdateBalancesAndCreateSuccessTransaction() {
        when(accountRepository.findByAccountNumber("1234567890")).thenReturn(Optional.of(senderAccount));
        when(accountRepository.findByAccountNumber("0987654321")).thenReturn(Optional.of(receiverAccount));
        when(transactionService.createSuccessTransaction(any(), any(), any(), any(), any())).thenReturn(new TransactionEntity());

        transferService.transfer(transactionDtoRequest);

        assertEquals(BigDecimal.valueOf(900.00), senderAccount.getBalance());
        assertEquals(BigDecimal.valueOf(600.00), receiverAccount.getBalance());
        verify(accountRepository, times(1)).save(senderAccount);
        verify(accountRepository, times(1)).save(receiverAccount);
        verify(transactionService, times(1)).createSuccessTransaction(senderAccount, receiverAccount, BigDecimal.valueOf(100.00), BigDecimal.valueOf(900.00), OperationType.TRANSFER);
    }

    @Test
    void transfer_shouldThrowAccountNotFoundException_whenSenderNotFound() {
        when(accountRepository.findByAccountNumber("1234567890")).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> transferService.transfer(transactionDtoRequest));
    }

    @Test
    void transfer_shouldCreateFailedTransaction_whenExceptionOccurs() {
        when(accountRepository.findByAccountNumber("1234567890")).thenReturn(Optional.of(senderAccount));
        when(accountRepository.findByAccountNumber("0987654321")).thenReturn(Optional.of(receiverAccount));
        doThrow(new RuntimeException("Test Exception")).when(transferValidator).validate(any(), any(), any());

        transferService.transfer(transactionDtoRequest);

        verify(transactionService, times(1)).createFailedTransaction(senderAccount, receiverAccount, BigDecimal.valueOf(100.00), BigDecimal.valueOf(1000.00), OperationType.TRANSFER, "Test Exception");
    }
}
