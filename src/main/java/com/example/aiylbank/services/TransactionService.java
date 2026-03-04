package com.example.aiylbank.services;

import com.example.aiylbank.entity.Account;
import com.example.aiylbank.entity.TransactionEntity;
import com.example.aiylbank.enums.OperationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    TransactionEntity createSuccessTransaction(Account sender, Account receiver, BigDecimal amount, BigDecimal balanceAfterOp ,OperationType type);
    TransactionEntity createFailedTransaction(Account sender, Account receiver, BigDecimal amount, BigDecimal balanceAfterOp ,OperationType type, String failureReason);

    TransactionEntity findById(Long id);
    List<TransactionEntity> getStatement(String accountNumber, LocalDateTime from, LocalDateTime to, Pageable pageable);
}
