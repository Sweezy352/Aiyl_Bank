package com.example.aiylbank.services.impl;

import com.example.aiylbank.annotations.Loggable;
import com.example.aiylbank.entity.Account;
import com.example.aiylbank.entity.TransactionEntity;
import com.example.aiylbank.enums.OperationType;
import com.example.aiylbank.enums.TransactionStatus;
import com.example.aiylbank.exceptions.TransactionNotFoundException;
import com.example.aiylbank.mapper.TransactionMapper;
import com.example.aiylbank.repository.TransactionRepository;
import com.example.aiylbank.services.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Override
    @Transactional
    @Loggable
    public TransactionEntity createSuccessTransaction(Account sender, Account receiver, BigDecimal amount, BigDecimal balanceAfterOp ,OperationType type) {
        TransactionEntity transaction = transactionMapper.toEntity(sender, receiver, amount, balanceAfterOp ,type ,TransactionStatus.SUCCESS, null);
        return transactionRepository.save(transaction);
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    @Loggable
    public TransactionEntity createFailedTransaction(Account sender, Account receiver, BigDecimal amount, BigDecimal balanceAfterOp ,OperationType type,String failureReason) {
        TransactionEntity transaction = transactionMapper.toEntity(sender, receiver, amount, balanceAfterOp ,type, TransactionStatus.FAILED, failureReason);
        return transactionRepository.save(transaction);
    }

    @Override
    public TransactionEntity findById(Long id) {
        return transactionRepository.findById(id).orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));
    }

    @Override
    public List<TransactionEntity> getStatement(String accountNumber, LocalDateTime from, LocalDateTime to, Pageable pageable) {
        return transactionRepository.getStatement(accountNumber, from, to, pageable).getContent();
    }
}
