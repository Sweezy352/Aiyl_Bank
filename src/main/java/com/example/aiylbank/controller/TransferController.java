package com.example.aiylbank.controller;

import com.example.aiylbank.dto.request.TransactionDtoRequest;
import com.example.aiylbank.dto.response.TransactionDtoResponse;
import com.example.aiylbank.mapper.TransactionMapper;
import com.example.aiylbank.services.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {
    private final TransferService transferService;
    private final TransactionMapper transactionMapper;

    @PostMapping("/deposit")
    public ResponseEntity<TransactionDtoResponse> deposit(@RequestBody TransactionDtoRequest request) {
        return ResponseEntity.ok(transactionMapper.toDtoResponse(transferService.deposit(request)));
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionDtoResponse> transfer(@RequestBody TransactionDtoRequest request) {
        return ResponseEntity.ok(transactionMapper.toDtoResponse(transferService.transfer(request)));
    }

}
