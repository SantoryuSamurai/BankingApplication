package com.ofss.AccountService.service;

import com.ofss.AccountService.DTO.*;
import com.ofss.AccountService.models.*;
import com.ofss.AccountService.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final BankRepository bankRepository;
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    @Override
    public List<TransactionResponseDTO> getTransactionsByBank(Long bankId) {
        Bank bank = bankRepository.findById(bankId).orElseThrow(() -> new RuntimeException("Bank not found"));
        List<Transaction> transactions = transactionRepository.findByBank(bank);
        return mapToDTOList(transactions);
    }

    @Override
    public List<TransactionResponseDTO> getTransactionsByAccount(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        List<Transaction> transactions = transactionRepository.findByAccount(account);
        return mapToDTOList(transactions);
    }

    @Override
    public List<TransactionResponseDTO> getTransactionsByCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));
        List<Transaction> transactions = transactionRepository.findByCustomer(customer);
        return mapToDTOList(transactions);
    }

    private List<TransactionResponseDTO> mapToDTOList(List<Transaction> transactions) {
        return transactions.stream()
                .map(this::mapToTransactionResponseDTO)
                .collect(Collectors.toList());
    }

    private TransactionResponseDTO mapToTransactionResponseDTO(Transaction txn) {
        AccountResponseDTO sourceAccDTO = txn.getSourceAccount() != null
                ? new AccountResponseDTO(txn.getSourceAccount().getId(),
                txn.getSourceAccount().getAccountNumber(),
                txn.getSourceAccount().getAccountType(),
                txn.getSourceAccount().getBalance(),
                txn.getSourceAccount().getStatus(),
                txn.getSourceAccount().getCreatedAt(),
                null,
                null)
                : null;

        AccountResponseDTO targetAccDTO = txn.getTargetAccount() != null
                ? new AccountResponseDTO(txn.getTargetAccount().getId(),
                txn.getTargetAccount().getAccountNumber(),
                txn.getTargetAccount().getAccountType(),
                txn.getTargetAccount().getBalance(),
                txn.getTargetAccount().getStatus(),
                txn.getTargetAccount().getCreatedAt(),
                null,
                null)
                : null;

        BankResponseDTO bankDTO = txn.getBank() != null
                ? new BankResponseDTO(txn.getBank().getId(),
                txn.getBank().getName(),
                txn.getBank().getIfsc_code(),
                txn.getBank().getAddress())
                : null;

        return new TransactionResponseDTO(txn.getId(), txn.getAmount(), txn.getType(),
                txn.getTimestamp(), txn.getRemarks(),
                sourceAccDTO, targetAccDTO, bankDTO);
    }

}

