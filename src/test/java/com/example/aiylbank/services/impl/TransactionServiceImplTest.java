package com.example.aiylbank.services.impl;

import com.example.aiylbank.entity.Account;
import com.example.aiylbank.entity.TransactionEntity;
import com.example.aiylbank.enums.OperationType;
import com.example.aiylbank.enums.TransactionStatus;
import com.example.aiylbank.exceptions.TransactionNotFoundException;
import com.example.aiylbank.mapper.TransactionMapper;
import com.example.aiylbank.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Account senderAccount;
    private Account receiverAccount;
    private TransactionEntity successTransaction;
    private TransactionEntity failedTransaction;

    @BeforeEach
    void setUp() {
        senderAccount = new Account();
        senderAccount.setId(1L);
        senderAccount.setAccountNumber("1234567890");
        senderAccount.setBalance(BigDecimal.valueOf(1000.00));

        receiverAccount = new Account();
        receiverAccount.setId(2L);
        receiverAccount.setAccountNumber("0987654321");
        receiverAccount.setBalance(BigDecimal.valueOf(500.00));

        successTransaction = new TransactionEntity();
        successTransaction.setId(1L);
        successTransaction.setSenderAccount(senderAccount);
        successTransaction.setReceiverAccount(receiverAccount);
        successTransaction.setAmount(BigDecimal.valueOf(100.00));
        successTransaction.setBalanceAfterOp(BigDecimal.valueOf(900.00));
        successTransaction.setOperationType(OperationType.TRANSFER);
        successTransaction.setStatus(TransactionStatus.SUCCESS);
        successTransaction.setCreatedAt(LocalDateTime.now());

        failedTransaction = new TransactionEntity();
        failedTransaction.setId(2L);
        failedTransaction.setSenderAccount(senderAccount);
        failedTransaction.setReceiverAccount((receiverAccount));
        failedTransaction.setAmount(BigDecimal.valueOf(200.00));
        failedTransaction.setBalanceAfterOp(BigDecimal.valueOf(1000.00));
        failedTransaction.setOperationType(OperationType.TRANSFER);
        failedTransaction.setStatus(TransactionStatus.FAILED);
        failedTransaction.setFailureReason("Insufficient funds");
        failedTransaction.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void createSuccessTransaction_shouldMapAndSaveTransaction() {
        when(transactionMapper.toEntity(any(Account.class), any(Account.class), any(BigDecimal.class), any(BigDecimal.class), any(OperationType.class), eq(TransactionStatus.SUCCESS), eq(null)))
                .thenReturn(successTransaction);
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(successTransaction);

        TransactionEntity result = transactionService.createSuccessTransaction(senderAccount, receiverAccount, BigDecimal.valueOf(100.00), BigDecimal.valueOf(900.00), OperationType.TRANSFER);

        assertNotNull(result);
        assertEquals(TransactionStatus.SUCCESS, result.getStatus());
        verify(transactionMapper, times(1)).toEntity(senderAccount, receiverAccount, BigDecimal.valueOf(100.00), BigDecimal.valueOf(900.00), OperationType.TRANSFER, TransactionStatus.SUCCESS, null);
        verify(transactionRepository, times(1)).save(successTransaction);
    }

    @Test
    void createFailedTransaction_shouldMapAndSaveTransaction() {
        when(transactionMapper.toEntity(any(Account.class), any(Account.class), any(BigDecimal.class), any(BigDecimal.class), any(OperationType.class), eq(TransactionStatus.FAILED), anyString()))
                .thenReturn(failedTransaction);
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(failedTransaction);

        TransactionEntity result = transactionService.createFailedTransaction(senderAccount, receiverAccount, BigDecimal.valueOf(200.00), BigDecimal.valueOf(1000.00), OperationType.TRANSFER, "Insufficient funds");

        assertNotNull(result);
        assertEquals(TransactionStatus.FAILED, result.getStatus());
        assertEquals("Insufficient funds", result.getFailureReason());
        verify(transactionMapper, times(1)).toEntity(senderAccount, receiverAccount, BigDecimal.valueOf(200.00), BigDecimal.valueOf(1000.00), OperationType.TRANSFER, TransactionStatus.FAILED, "Insufficient funds");
        verify(transactionRepository, times(1)).save(failedTransaction);
    }

    @Test
    void findById_shouldReturnTransaction_whenFound() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(successTransaction));

        TransactionEntity result = transactionService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(transactionRepository, times(1)).findById(1L);
    }

    @Test
    void findById_shouldThrowTransactionNotFoundException_whenNotFound() {
        when(transactionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.findById(99L));
        verify(transactionRepository, times(1)).findById(99L);
    }

    @Test
    void getStatement_shouldReturnListOfTransactions() {
        LocalDateTime from = LocalDateTime.now().minusDays(7);
        LocalDateTime to = LocalDateTime.now();
        Pageable pageable = Pageable.unpaged();
        List<TransactionEntity> transactions = Collections.singletonList(successTransaction);
        when(transactionRepository.getStatement(eq("1234567890"), any(LocalDateTime.class), any(LocalDateTime.class), eq(pageable)))
                .thenReturn(new PageImpl<>(transactions));

        List<TransactionEntity> result = transactionService.getStatement("1234567890", from, to, pageable);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(successTransaction, result.get(0));
        verify(transactionRepository, times(1)).getStatement("1234567890", from, to, pageable);
    }
}
