package com.example.aiylbank.entity;

import com.example.aiylbank.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Account extends BaseEntity{
    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal balance;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist(){
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        status = AccountStatus.ACTIVE;
        balance = BigDecimal.ZERO;
    }
}
