package com.example.aiylbank.repository;

import com.example.aiylbank.entity.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    @Query("select t from TransactionEntity t where " +
            "(t.senderAccount.accountNumber = :accNum or t.receiverAccount.accountNumber = :accNum)" +
            " and t.createdAt between :from and :to")
    Page<TransactionEntity> getStatement(
            @Param("accNum") String accountNumber,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to, Pageable pageable
    );
}
