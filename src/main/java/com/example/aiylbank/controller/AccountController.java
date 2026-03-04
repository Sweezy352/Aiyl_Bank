package com.example.aiylbank.controller;

import com.example.aiylbank.dto.request.AccountDtoRequest;
import com.example.aiylbank.dto.response.AccountDtoResponse;
import com.example.aiylbank.dto.response.TransactionDtoResponse;
import com.example.aiylbank.mapper.AccountMapper;
import com.example.aiylbank.mapper.TransactionMapper;
import com.example.aiylbank.services.AccountService;
import com.example.aiylbank.services.TransactionService;
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
public class AccountController {
    private final AccountMapper accountMapper;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @PostMapping
    public ResponseEntity<AccountDtoResponse> createAccount(@Valid @RequestBody AccountDtoRequest accountDtoRequest) {
        return new ResponseEntity<>(accountMapper.toDtoResponse(accountService.createAccount(accountMapper.toEntity(accountDtoRequest))), HttpStatus.CREATED);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountDtoResponse> getByAccountNumber(@PathVariable String accountNumber){
        return ResponseEntity.ok(accountMapper.toDtoResponse(accountService.getAccountByAccountNumber(accountNumber)));
    }

    @PatchMapping("/{accountNumber}/update-status")
    public ResponseEntity<AccountDtoResponse> updateStatus(@PathVariable String accountNumber, @RequestParam String status){
        return ResponseEntity.ok(accountMapper.toDtoResponse(accountService.updateStatus(accountNumber, status)));
    }

    @GetMapping("/{accountNumber}/statement")
    public ResponseEntity<List<TransactionDtoResponse>> getAccountStatement(
            @PathVariable String accountNumber,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(transactionMapper.toDtoResponseList(transactionService.getStatement(accountNumber, from, to, PageRequest.of(page, size, Sort.by("createdAt").descending()))));
    }
}
