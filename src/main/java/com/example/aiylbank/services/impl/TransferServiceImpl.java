package com.example.aiylbank.services.impl;

import com.example.aiylbank.annotations.Loggable;
import com.example.aiylbank.dto.request.TransactionDtoRequest;
import com.example.aiylbank.dto.response.TransactionDtoResponse;
import com.example.aiylbank.entity.Account;
import com.example.aiylbank.entity.TransactionEntity;
import com.example.aiylbank.enums.OperationType;
import com.example.aiylbank.enums.TransactionStatus;
import com.example.aiylbank.exceptions.AccountNotFoundException;
import com.example.aiylbank.exceptions.InsufficientFundsException;
import com.example.aiylbank.repository.AccountRepository;
import com.example.aiylbank.repository.TransactionRepository;
import com.example.aiylbank.services.TransactionService;
import com.example.aiylbank.services.TransferService;
import com.example.aiylbank.utils.TransferValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferServiceImpl implements TransferService {
    private final AccountRepository accountRepository;
    private final TransactionService transactionService;
    private final List<TransferValidator> transferValidators;

    @Override
    @Transactional
    @Loggable
    public TransactionEntity deposit(TransactionDtoRequest request) {
        Account receiver = accountRepository.findByAccountNumber(request.getReceiverAccountNumber()).orElseThrow(() -> new AccountNotFoundException("Receiver account not found"));
        try {
            log.info("Started depositing money with amount: {}, to account: {}", request.getAmount(), request.getReceiverAccountNumber());
            receiver.setBalance(receiver.getBalance().add(request.getAmount()));
            accountRepository.save(receiver);
            return transactionService.createSuccessTransaction(null, receiver, request.getAmount(), receiver.getBalance() ,OperationType.DEPOSIT);
        }catch (Exception ex){
            log.error("Error depositing money", ex);
            return transactionService.createFailedTransaction(null, receiver, request.getAmount(), receiver.getBalance() ,OperationType.DEPOSIT, ex.getMessage());
        }
    }

    @Override
    @Transactional
    @Loggable
    public TransactionEntity transfer(TransactionDtoRequest request) {
        Account sender = accountRepository.findByAccountNumber(request.getSenderAccountNumber()).orElseThrow(() -> new AccountNotFoundException("Sender account not found"));
        Account receiver = accountRepository.findByAccountNumber(request.getReceiverAccountNumber()).orElseThrow(() -> new AccountNotFoundException("Receiver account not found"));

        try {
            log.info("Started transferring money with amount: {}, from account: {} to account: {}", request.getAmount(), request.getSenderAccountNumber(), request.getReceiverAccountNumber());
            transferValidators.forEach(validator -> validator.validate(sender, receiver, request.getAmount()));
            sender.setBalance(sender.getBalance().subtract(request.getAmount()));
            receiver.setBalance(receiver.getBalance().add(request.getAmount()));
            accountRepository.save(sender);
            accountRepository.save(receiver);
            TransactionEntity transaction = transactionService.createSuccessTransaction(sender, receiver, request.getAmount(), sender.getBalance() ,OperationType.TRANSFER);
            log.info("Finished transferring money with amount: {}, from account: {} to account: {}", request.getAmount(), request.getSenderAccountNumber(), request.getReceiverAccountNumber());
            return transaction;
        }catch (Exception ex){
            log.error("Error transferring money", ex);
            return transactionService.createFailedTransaction(sender, receiver, request.getAmount(), sender.getBalance() ,OperationType.TRANSFER, ex.getMessage());
        }
    }
}
