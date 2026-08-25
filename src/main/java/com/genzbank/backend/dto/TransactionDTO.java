package com.genzbank.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.genzbank.backend.entity.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

    private Long id;

    private TransactionType type;

    private BigDecimal amount;

    private BigDecimal balanceAfter;

    private LocalDateTime transactionDate;
}