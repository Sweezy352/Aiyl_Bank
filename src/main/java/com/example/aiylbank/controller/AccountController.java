package com.example.aiylbank.controller;

import com.example.aiylbank.dto.request.AccountDtoRequest;
import com.example.aiylbank.dto.response.AccountDtoResponse;
import com.example.aiylbank.dto.response.TransactionDtoResponse;
import com.example.aiylbank.mapper.AccountMapper;
import com.example.aiylbank.mapper.TransactionMapper;
import com.example.aiylbank.services.AccountService;
import com.example.aiylbank.services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Tag(name = "Account Controller", description = "API для управления банковскими счетами")
public class AccountController {
    private final AccountMapper accountMapper;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @PostMapping
    @Operation(summary = "Создать счет", description = "Создать новый банковский счет")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Счет успешно создан",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountDtoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<AccountDtoResponse> createAccount(
            @Parameter(description = "Данные для создания счета", required = true)
            @Valid @RequestBody AccountDtoRequest accountDtoRequest) {
        return new ResponseEntity<>(accountMapper.toDtoResponse(
                accountService.createAccount(
                        accountMapper.toEntity(accountDtoRequest)
                )
        ), HttpStatus.CREATED);
    }

    @GetMapping("/{accountNumber}")
    @Operation(summary = "Получить счет по номеру", description = "Найти счет по его уникальному номеру")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Счет найден",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountDtoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Счет не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<AccountDtoResponse> getByAccountNumber(
            @Parameter(description = "Номер счета", required = true)
            @PathVariable String accountNumber){
        return ResponseEntity.ok(accountMapper.toDtoResponse(accountService.getAccountByAccountNumber(accountNumber)));
    }

    @PatchMapping("/{accountNumber}/update-status")
    @Operation(summary = "Обновить статус счета", description = "Изменить статус счета (ACTIVE/BLOCKED)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус успешно обновлен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountDtoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некорректный статус"),
            @ApiResponse(responseCode = "404", description = "Счет не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<AccountDtoResponse> updateStatus(
            @Parameter(description = "Номер счета", required = true)
            @PathVariable String accountNumber,
            @Parameter(description = "Новый статус счета", required = true)
            @RequestParam String status){
        return ResponseEntity.ok(accountMapper.toDtoResponse(accountService.updateStatus(accountNumber, status)));
    }

    @GetMapping("/{accountNumber}/statement")
    @Operation(summary = "Получить выписку по счету", description = "Получить выписку по счету с фильтрацией по дате и пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Выписка успешно получена",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionDtoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса"),
            @ApiResponse(responseCode = "404", description = "Счет не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<TransactionDtoResponse>> getAccountStatement(
            @Parameter(description = "Номер счета", required = true)
            @PathVariable String accountNumber,
            @Parameter(description = "Начальная дата периода", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @Parameter(description = "Конечная дата периода", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime to,
            @Parameter(description = "Номер страницы (по умолчанию 0)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы (по умолчанию 10)")
            @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(transactionMapper.toDtoResponseList(
                transactionService.getStatement(
                        accountNumber,
                        from,
                        to,
                        PageRequest.of(page, size, Sort.by("createdAt").descending()))
                )
        );
    }
}
