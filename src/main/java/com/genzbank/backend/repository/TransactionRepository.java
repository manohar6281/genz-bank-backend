package com.genzbank.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.genzbank.backend.entity.Account;
import com.genzbank.backend.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccountOrderByTransactionDateDesc(Account account);
}