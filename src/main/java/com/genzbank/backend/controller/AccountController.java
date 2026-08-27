package com.genzbank.backend.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.genzbank.backend.dto.AccountResponse;
import com.genzbank.backend.dto.TransactionDTO;
import com.genzbank.backend.entity.Account;
import com.genzbank.backend.entity.AccountType;
import com.genzbank.backend.entity.Transaction;
import com.genzbank.backend.entity.User;
import com.genzbank.backend.service.AccountService;
import com.genzbank.backend.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://genz-bank-frontend.vercel.app"
})
public class AccountController {

    private final AccountService accountService;
    private final UserService userService;

    // =====================================================
    // CREATE ACCOUNT
    // =====================================================

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @RequestParam Long userId,
            @RequestParam AccountType type) {

        User user = userService.getUserById(userId);

        Account account =
                accountService.createAccount(user, type);

        return ResponseEntity.ok(
                convertToResponse(account)
        );
    }

    // =====================================================
    // GET ALL ACCOUNTS FOR USER
    // =====================================================

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getUserAccounts(
            @RequestParam Long userId) {

        User user = userService.getUserById(userId);

        List<Account> accounts =
                accountService.getAccountsByUser(user);

        List<AccountResponse> responses =
                accounts.stream()
                        .map(this::convertToResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    // =====================================================
    // GET SINGLE ACCOUNT
    // =====================================================

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccount(
            @PathVariable Long id) {

        Account account =
                accountService.getAccountById(id);

        return ResponseEntity.ok(
                convertToResponse(account)
        );
    }

    // =====================================================
    // DEPOSIT
    // =====================================================

    @PostMapping("/{id}/deposit")
    public ResponseEntity<?> deposit(
            @PathVariable Long id,
            @RequestParam BigDecimal amount) {

        try {

            Account account =
                    accountService.deposit(id, amount);

            return ResponseEntity.ok(
                    convertToResponse(account)
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // =====================================================
    // WITHDRAW
    // =====================================================

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<?> withdraw(
            @PathVariable Long id,
            @RequestParam BigDecimal amount) {

        try {

            Account account =
                    accountService.withdraw(id, amount);

            return ResponseEntity.ok(
                    convertToResponse(account)
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // =====================================================
    // TRANSACTION HISTORY
    // =====================================================

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<TransactionDTO>> getTransactions(
            @PathVariable Long id) {

        List<Transaction> transactions =
                accountService.getTransactions(id);

        List<TransactionDTO> response =
                transactions.stream()
                        .map(transaction ->
                                new TransactionDTO(
                                        transaction.getId(),
                                        transaction.getType(),
                                        transaction.getAmount(),
                                        transaction.getBalanceAfter(),
                                        transaction.getTransactionDate()
                                )
                        )
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // =====================================================
    // ACCOUNT RESPONSE
    // =====================================================

    private AccountResponse convertToResponse(
            Account account) {

        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getType(),
                account.getBalance(),
                account.getStatus(),
                account.getUser().getId()
        );
    }
}