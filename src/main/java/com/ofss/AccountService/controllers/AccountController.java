package com.ofss.AccountService.controllers;

import com.ofss.AccountService.DTO.AccountResponseDTO;
import com.ofss.AccountService.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/account")
    public ResponseEntity<?> getAllAccounts() {
        List<AccountResponseDTO> accounts = accountService.getAllAccounts();
        if (accounts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Accounts Created yet");
        }
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/account/id/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable Long id) {
        Optional<AccountResponseDTO> accountOpt = accountService.getAccountById(id);
        if (accountOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        }
        return ResponseEntity.ok(accountOpt.get());
    }
}
