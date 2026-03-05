package com.example.aiylbank.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Запрос на выполнение транзакции")
public class TransactionDtoRequest {
    @NotNull(message = "amount is required")
    @Positive(message = "amount must be greater than zero")
    @Schema(description = "Сумма транзакции", example = "1000.50", required = true)
    private BigDecimal amount;
    
    @Schema(description = "Номер счета отправителя (для переводов)", example = "1234567890")
    private String senderAccountNumber;
    
    @NotBlank(message = "receiver account number is required")
    @Schema(description = "Номер счета получателя", example = "0987654321", required = true)
    private String receiverAccountNumber;
}
