package com.genzbank.backend.dto;

import java.math.BigDecimal;

import com.genzbank.backend.entity.AccountType;

import lombok.AllArgsConstructor;

import lombok.Data;

import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor

@AllArgsConstructor

public class AccountResponse {

    private Long id;

    private String accountNumber;

    private AccountType type;

    private BigDecimal balance;

    private String status;

    private Long userId;

}