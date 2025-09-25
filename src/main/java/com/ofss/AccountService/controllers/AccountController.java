package com.ofss.AccountService.controllers;

import com.ofss.AccountService.DTO.*;
import com.ofss.AccountService.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/admin/account")
    public ResponseEntity<?> getAllAccounts() {
        List<AccountResponseDTO> accounts = accountService.getAllAccounts();
        if (accounts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Accounts Created yet");
        }
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/admin/account/id/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable Long id) {
        Optional<AccountResponseDTO> accountOpt = accountService.getAccountById(id);
        if (accountOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        }
        return ResponseEntity.ok(accountOpt.get());
    }

    @PostMapping("/admin/account")
    public ResponseEntity<?> createAccount(@RequestBody AccountPostDTO accountPostDTO) {
        try {
            AccountResponseDTO created = accountService.createAccount(accountPostDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred. Please try again later.");
        }
    }

    @PutMapping("/admin/account/id/{accountId}")
    public ResponseEntity<?> updateAccount(@PathVariable Long accountId, @RequestBody AccountPostDTO accountPostDTO) {
        try {
            AccountResponseDTO updatedAccount = accountService.updateAccount(accountId, accountPostDTO);
            return ResponseEntity.ok(updatedAccount);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred. Please try again later.");
        }
    }

    @PatchMapping("/admin/account/id/{accountId}")
    public ResponseEntity<?> patchAccount(@PathVariable Long accountId, @RequestBody AccountPostDTO accountPostDTO) {
        try {
            AccountResponseDTO updatedAccount = accountService.patchAccount(accountId, accountPostDTO);
            return ResponseEntity.ok(updatedAccount);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred. Please try again later.");
        }
    }

    @PostMapping("/deposit/account/id/{accountId}")
    public ResponseEntity<?> depositAmount(@PathVariable Long accountId, @RequestBody DepositRequestDTO depositRequestDTO) {
        try {
            AccountResponseDTO updatedAccount = accountService.depositAmount(accountId, depositRequestDTO.getDepositAmount());
            return ResponseEntity.ok(updatedAccount);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred. Please try again later.");
        }
    }

    @PostMapping("/withdraw/account/id/{accountId}")
    public ResponseEntity<?> withdrawAmount(@PathVariable Long accountId, @RequestBody WithdrawRequestDTO withdrawRequestDTO) {
        try {
            AccountResponseDTO updatedAccount = accountService.withdrawAmount(accountId, withdrawRequestDTO.getWithdrawAmount());
            return ResponseEntity.ok(updatedAccount);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred. Please try again later.");
        }
    }

    @PostMapping("/transfer/{fromAccountId}/{toAccountId}")
    public ResponseEntity<?> transferAmount(
            @PathVariable Long fromAccountId,
            @PathVariable Long toAccountId,
            @RequestBody TransferRequestDTO transferRequestDTO) {
        try {
            AccountResponseDTO updatedAccount = accountService.transferAmount(
                    fromAccountId, toAccountId, transferRequestDTO.getTransferAmount());
            return ResponseEntity.ok(updatedAccount);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred. Please try again later.");
        }
    }



}
