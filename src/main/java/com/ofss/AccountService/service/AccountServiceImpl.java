package com.ofss.AccountService.service;

import com.ofss.AccountService.DTO.AccountResponseDTO;
import com.ofss.AccountService.DTO.BankResponseDTO;
import com.ofss.AccountService.DTO.CustomerResponseDTO;
import com.ofss.AccountService.models.Account;
import com.ofss.AccountService.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;

    @Override
    public List<AccountResponseDTO> getAllAccounts() {
        List<Account> accounts = (List<Account>) accountRepository.findAll();

        return accounts.stream().map(acc -> new AccountResponseDTO(
                acc.getId(),
                acc.getAccountNumber(),
//                acc.getBank().getName(),
                acc.getAccountType(),
                acc.getBalance(),
                acc.getStatus(),
                acc.getCreatedAt(),


                // map Customer to CustomerDTO
                new CustomerResponseDTO(
                        acc.getCustomer().getCustomerId(),
                        acc.getCustomer().getName(),
                        acc.getCustomer().getEmail(),
                        acc.getCustomer().getPhone(),
                        acc.getCustomer().getAddress()
                ),
                // map Bank to BankDTO
                new BankResponseDTO(
                        acc.getBank().getId(),
                        acc.getBank().getName(),
                        acc.getBank().getIfsc_code(),
                        acc.getBank().getAddress()
                )
        )).collect(Collectors.toList());

    }
    @Override
    public Optional<AccountResponseDTO> getAccountById(Long id) {
        return accountRepository.findById(id).map(acc -> new AccountResponseDTO(
                acc.getId(),
                acc.getAccountNumber(),
//                acc.getBank().getName(),
                acc.getAccountType(),
                acc.getBalance(),
                acc.getStatus(),
                acc.getCreatedAt(),
                new CustomerResponseDTO(
                        acc.getCustomer().getCustomerId(),
                        acc.getCustomer().getName(),
                        acc.getCustomer().getEmail(),
                        acc.getCustomer().getPhone(),
                        acc.getCustomer().getAddress()
                ),
                new BankResponseDTO(
                        acc.getBank().getId(),
                        acc.getBank().getName(),
                        acc.getBank().getIfsc_code(),
                        acc.getBank().getAddress()
                )
        ));
    }
}
