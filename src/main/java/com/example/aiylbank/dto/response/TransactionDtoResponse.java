package com.example.aiylbank.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDtoResponse {
    private LocalDateTime createdAt;
    private String operationType;
    private BigDecimal amount;
    private BigDecimal balanceAfterOp;
    private String status;
    private String failureReason;
    private String senderAccountNumber;
    private String receiverAccountNumber;
}
