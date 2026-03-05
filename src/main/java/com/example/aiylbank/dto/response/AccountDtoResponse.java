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
@Schema(description = "Ответ с информацией о счете")
public class AccountDtoResponse {
    @Schema(description = "Номер счета", example = "1234567890")
    private String accountNumber;
    
    @Schema(description = "Текущий баланс", example = "10000.50")
    private BigDecimal balance;
    
    @Schema(description = "Статус счета", example = "ACTIVE", allowableValues = {"ACTIVE", "BLOCKED"})
    private String status;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Дата создания счета", example = "2026-03-04 15:30:00")
    private LocalDateTime createdAt;
}
