package com.genzbank.backend.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.genzbank.backend.entity.Account;
import com.genzbank.backend.entity.AccountType;
import com.genzbank.backend.entity.Transaction;
import com.genzbank.backend.entity.TransactionType;
import com.genzbank.backend.entity.User;
import com.genzbank.backend.repository.AccountRepository;
import com.genzbank.backend.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public Account createAccount(User user, AccountType type) {

        Account account = new Account();

        account.setAccountNumber(generateAccountNumber());
        account.setType(type);
        account.setBalance(BigDecimal.ZERO);
        account.setStatus("ACTIVE");
        account.setUser(user);

        return accountRepository.save(account);
    }

    public List<Account> getAccountsByUser(User user) {
        return accountRepository.findByUser(user);
    }

    public Account getAccountById(Long id) {

        return accountRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Account not found"));
    }

    @Transactional
    public Account deposit(Long accountId, BigDecimal amount) {

        validateAmount(amount);

        Account account = getAccountById(accountId);

        if (!"ACTIVE".equalsIgnoreCase(account.getStatus())) {
            throw new RuntimeException("Account is not active");
        }

        BigDecimal newBalance =
                account.getBalance().add(amount);

        account.setBalance(newBalance);

        Account savedAccount =
                accountRepository.save(account);

        Transaction transaction = new Transaction();

        transaction.setType(TransactionType.DEPOSIT);
        transaction.setAmount(amount);
        transaction.setBalanceAfter(newBalance);
        transaction.setAccount(savedAccount);

        transactionRepository.save(transaction);

        return savedAccount;
    }

    @Transactional
    public Account withdraw(Long accountId, BigDecimal amount) {

        validateAmount(amount);

        Account account = getAccountById(accountId);

        if (!"ACTIVE".equalsIgnoreCase(account.getStatus())) {
            throw new RuntimeException("Account is not active");
        }

        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException(
                    "Insufficient balance");
        }

        BigDecimal newBalance =
                account.getBalance().subtract(amount);

        account.setBalance(newBalance);

        Account savedAccount =
                accountRepository.save(account);

        Transaction transaction = new Transaction();

        transaction.setType(TransactionType.WITHDRAW);
        transaction.setAmount(amount);
        transaction.setBalanceAfter(newBalance);
        transaction.setAccount(savedAccount);

        transactionRepository.save(transaction);

        return savedAccount;
    }

    public List<Transaction> getTransactions(Long accountId) {

        Account account = getAccountById(accountId);

        return transactionRepository
                .findByAccountOrderByTransactionDateDesc(account);
    }

    private void validateAmount(BigDecimal amount) {

        if (amount == null) {
            throw new RuntimeException(
                    "Amount is required");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException(
                    "Amount must be greater than zero");
        }
    }

    private String generateAccountNumber() {

        long number =
                1000000000L
                + (long) (Math.random() * 900000000L);

        return String.valueOf(number);
    }
}