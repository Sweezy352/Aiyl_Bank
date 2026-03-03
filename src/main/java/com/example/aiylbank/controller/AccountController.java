package com.example.aiylbank.controller;

import com.example.aiylbank.dto.request.AccountDtoRequest;
import com.example.aiylbank.dto.response.AccountDtoResponse;
import com.example.aiylbank.mapper.AccountMapper;
import com.example.aiylbank.services.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountMapper accountMapper;
    private final AccountService accountService;

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

    @PostMapping("/{accoundNumber}/deposit")
    public ResponseEntity<AccountDtoResponse> deposit(@PathVariable String accoundNumber, @RequestParam BigDecimal amount){
        return null;
    }
}
