package com.example.aiylbank.controller;

import com.example.aiylbank.dto.request.TransactionDtoRequest;
import com.example.aiylbank.dto.response.TransactionDtoResponse;
import com.example.aiylbank.mapper.TransactionMapper;
import com.example.aiylbank.services.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
@Tag(name = "Transfer Controller", description = "API для выполнения переводов и депозитов")
public class TransferController {
    private final TransferService transferService;
    private final TransactionMapper transactionMapper;

    @PostMapping("/deposit")
    @Operation(summary = "Выполнить депозит", description = "Пополнить счет указанной суммой")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Депозит успешно выполнен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionDtoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "404", description = "Счет не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<TransactionDtoResponse> deposit(
            @Parameter(description = "Данные для выполнения депозита", required = true)
            @RequestBody TransactionDtoRequest request) {
        return ResponseEntity.ok(transactionMapper.toDtoResponse(transferService.deposit(request)));
    }

    @PostMapping("/transfer")
    @Operation(summary = "Выполнить перевод", description = "Перевести средства с одного счета на другой")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Перевод успешно выполнен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionDtoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "404", description = "Счет не найден"),
            @ApiResponse(responseCode = "409", description = "Недостаточно средств или счет заблокирован"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<TransactionDtoResponse> transfer(
            @Parameter(description = "Данные для выполнения перевода", required = true)
            @RequestBody TransactionDtoRequest request) {
        return ResponseEntity.ok(transactionMapper.toDtoResponse(transferService.transfer(request)));
    }

}
