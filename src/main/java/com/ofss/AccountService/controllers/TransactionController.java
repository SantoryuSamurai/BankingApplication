package com.ofss.AccountService.controllers;

import com.ofss.AccountService.DTO.TransactionResponseDTO;
import com.ofss.AccountService.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/bank/{bankId}")
    public List<TransactionResponseDTO> getBankTransactions(@PathVariable Long bankId) {
        return transactionService.getTransactionsByBank(bankId);
    }

    @GetMapping("/account/{accountId}")
    public List<TransactionResponseDTO> getAccountTransactions(@PathVariable Long accountId) {
        return transactionService.getTransactionsByAccount(accountId);
    }

    @GetMapping("/customer/{customerId}")
    public List<TransactionResponseDTO> getCustomerTransactions(@PathVariable Long customerId) {
        return transactionService.getTransactionsByCustomer(customerId);
    }
}
