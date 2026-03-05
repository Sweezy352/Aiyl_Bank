package com.example.aiylbank.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Запрос на создание счета")
public class AccountDtoRequest {
    @NotBlank(message = "Account number is required")
    @Size(min = 5, max = 20, message = "Account number must be between 5 and 20 characters")
    @Pattern(regexp = "^\\d+$", message = "Account number must contain only digits")
    @Schema(description = "Номер счета", example = "1234567890", required = true)
    private String accountNumber;
}
