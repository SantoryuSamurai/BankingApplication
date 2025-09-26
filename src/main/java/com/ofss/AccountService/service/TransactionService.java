package com.ofss.AccountService.service;

import com.ofss.AccountService.DTO.TransactionResponseDTO;

import java.util.List;

public interface TransactionService {
    List<TransactionResponseDTO> getTransactionsByBank(Long bankId);
    List<TransactionResponseDTO> getTransactionsByAccount(Long accountId);
    List<TransactionResponseDTO> getTransactionsByCustomer(Long customerId);
}
