package com.example.aiylbank.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Ответ с информацией о транзакции")
public class TransactionDtoResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Дата и время операции", example = "2026-03-04 15:30:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Тип операции", example = "TRANSFER", allowableValues = {"TRANSFER", "DEPOSIT"})
    private String operationType;
    
    @Schema(description = "Сумма операции", example = "1000.50")
    private BigDecimal amount;
    
    @Schema(description = "Баланс после операции", example = "9000.50")
    private BigDecimal balanceAfterOp;
    
    @Schema(description = "Статус транзакции", example = "SUCCESS", allowableValues = {"SUCCESS", "FAILED"})
    private String status;
    
    @Schema(description = "Причина неудачи (если статус FAILED)", example = "Insufficient funds")
    private String failureReason;
    
    @Schema(description = "Номер счета отправителя", example = "1234567890")
    private String senderAccountNumber;
    
    @Schema(description = "Номер счета получателя", example = "0987654321")
    private String receiverAccountNumber;
}
