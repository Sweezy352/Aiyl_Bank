package com.example.aiylbank.services;

import com.example.aiylbank.dto.request.TransactionDtoRequest;
import com.example.aiylbank.dto.response.TransactionDtoResponse;
import com.example.aiylbank.entity.TransactionEntity;

public interface TransferService {
    TransactionEntity deposit(TransactionDtoRequest request);
    TransactionEntity transfer(TransactionDtoRequest request);
}
