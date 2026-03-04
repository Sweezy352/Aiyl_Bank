package com.example.aiylbank.mapper;

import com.example.aiylbank.dto.request.TransactionDtoRequest;
import com.example.aiylbank.dto.response.TransactionDtoResponse;
import com.example.aiylbank.entity.Account;
import com.example.aiylbank.entity.TransactionEntity;
import com.example.aiylbank.enums.OperationType;
import com.example.aiylbank.enums.TransactionStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", source = "status")
    @Mapping(target = "operationType", source = "operationType")
    @Mapping(target = "createdAt", ignore = true)
    TransactionEntity toEntity(Account senderAccount, Account receiverAccount, BigDecimal amount, BigDecimal balanceAfterOp ,OperationType operationType, TransactionStatus status, String failureReason);
    @Mapping(target = "senderAccountNumber", source = "senderAccount.accountNumber")
    @Mapping(target = "receiverAccountNumber", source = "receiverAccount.accountNumber")
    TransactionDtoResponse toDtoResponse(TransactionEntity entity);
    List<TransactionDtoResponse> toDtoResponseList(List<TransactionEntity> transactionEntities);
}
