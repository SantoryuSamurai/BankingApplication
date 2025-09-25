package com.ofss.AccountService.service;

import com.ofss.AccountService.DTO.AccountPostDTO;
import com.ofss.AccountService.DTO.AccountResponseDTO;
import com.ofss.AccountService.DTO.BankResponseDTO;
import com.ofss.AccountService.DTO.CustomerResponseDTO;
import com.ofss.AccountService.models.Account;
import com.ofss.AccountService.models.AccountStatus;
import com.ofss.AccountService.models.Bank;
import com.ofss.AccountService.models.Customer;
import com.ofss.AccountService.repository.AccountRepository;
import com.ofss.AccountService.repository.BankRepository;
import com.ofss.AccountService.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final BankRepository bankRepository;

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

    @Override
    public AccountResponseDTO createAccount(AccountPostDTO accountPostDTO) {
        Customer customer = customerRepository.findById(accountPostDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Bank bank = bankRepository.findById(accountPostDTO.getBankId())
                .orElseThrow(() -> new RuntimeException("Bank not found"));

        Account account = new Account();
        account.setAccountNumber(accountPostDTO.getAccountNumber());
        account.setAccountType(accountPostDTO.getAccountType());
        account.setBalance(accountPostDTO.getBalance());
        account.setStatus((accountPostDTO.getStatus()));
        account.setCustomer(customer);
        account.setBank(bank);

        Account saved = accountRepository.save(account);

        // Map saved entity to response DTO
        return new AccountResponseDTO(
                saved.getId(),
                saved.getAccountNumber(),
                saved.getAccountType(),
                saved.getBalance(),
                saved.getStatus(),
                saved.getCreatedAt(),
                new CustomerResponseDTO(
                        customer.getCustomerId(),
                        customer.getName(),
                        customer.getEmail(),
                        customer.getPhone(),
                        customer.getAddress()
                ),
                new BankResponseDTO(
                        bank.getId(),
                        bank.getName(),
                        bank.getIfsc_code(),
                        bank.getAddress()
                )
        );
    }

    @Override
    public AccountResponseDTO updateAccount(Long accountId, AccountPostDTO accountPostDTO) {
        return accountRepository.findById(accountId)
                .map(existingAccount -> {
                    Customer customer = customerRepository.findById(accountPostDTO.getCustomerId())
                            .orElseThrow(() -> new RuntimeException("Customer not found"));

                    Bank bank = bankRepository.findById(accountPostDTO.getBankId())
                            .orElseThrow(() -> new RuntimeException("Bank not found"));

                    existingAccount.setAccountNumber(accountPostDTO.getAccountNumber());
                    existingAccount.setAccountType(accountPostDTO.getAccountType());
                    existingAccount.setBalance(accountPostDTO.getBalance());
                    existingAccount.setStatus(accountPostDTO.getStatus());
                    existingAccount.setCustomer(customer);
                    existingAccount.setBank(bank);

                    Account savedAccount = accountRepository.save(existingAccount);

                    return new AccountResponseDTO(
                            savedAccount.getId(),
                            savedAccount.getAccountNumber(),
                            savedAccount.getAccountType(),
                            savedAccount.getBalance(),
                            savedAccount.getStatus(),
                            savedAccount.getCreatedAt(),
                            new CustomerResponseDTO(
                                    customer.getCustomerId(),
                                    customer.getName(),
                                    customer.getEmail(),
                                    customer.getPhone(),
                                    customer.getAddress()
                            ),
                            new BankResponseDTO(
                                    bank.getId(),
                                    bank.getName(),
                                    bank.getIfsc_code(),
                                    bank.getAddress()
                            )
                    );
                })
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @Override
    public AccountResponseDTO patchAccount(Long accountId, AccountPostDTO accountPostDTO) {
        return accountRepository.findById(accountId)
                .map(existingAccount -> {
                    if (accountPostDTO.getAccountNumber() != null) {
                        existingAccount.setAccountNumber(accountPostDTO.getAccountNumber());
                    }
                    if (accountPostDTO.getAccountType() != null) {
                        existingAccount.setAccountType(accountPostDTO.getAccountType());
                    }
                    if (accountPostDTO.getBalance() != null) {
                        existingAccount.setBalance(accountPostDTO.getBalance());
                    }
                    if (accountPostDTO.getStatus() != null) {
                        existingAccount.setStatus(accountPostDTO.getStatus());
                    }
                    if (accountPostDTO.getCustomerId() != null) {
                        Customer customer = customerRepository.findById(accountPostDTO.getCustomerId())
                                .orElseThrow(() -> new RuntimeException("Customer not found"));
                        existingAccount.setCustomer(customer);
                    }
                    if (accountPostDTO.getBankId() != null) {
                        Bank bank = bankRepository.findById(accountPostDTO.getBankId())
                                .orElseThrow(() -> new RuntimeException("Bank not found"));
                        existingAccount.setBank(bank);
                    }
                    Account updatedAccount = accountRepository.save(existingAccount);

                    Customer customer = updatedAccount.getCustomer();
                    Bank bank = updatedAccount.getBank();

                    return new AccountResponseDTO(
                            updatedAccount.getId(),
                            updatedAccount.getAccountNumber(),
                            updatedAccount.getAccountType(),
                            updatedAccount.getBalance(),
                            updatedAccount.getStatus(),
                            updatedAccount.getCreatedAt(),
                            new CustomerResponseDTO(
                                    customer.getCustomerId(),
                                    customer.getName(),
                                    customer.getEmail(),
                                    customer.getPhone(),
                                    customer.getAddress()
                            ),
                            new BankResponseDTO(
                                    bank.getId(),
                                    bank.getName(),
                                    bank.getIfsc_code(),
                                    bank.getAddress()
                            )
                    );
                })
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @Override
    public AccountResponseDTO depositAmount(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setBalance(account.getBalance().add(amount));
        Account savedAccount = accountRepository.save(account);

        return new AccountResponseDTO(
                savedAccount.getId(),
                savedAccount.getAccountNumber(),
                savedAccount.getAccountType(),
                savedAccount.getBalance(),
                savedAccount.getStatus(),
                savedAccount.getCreatedAt(),
                new CustomerResponseDTO(
                        savedAccount.getCustomer().getCustomerId(),
                        savedAccount.getCustomer().getName(),
                        savedAccount.getCustomer().getEmail(),
                        savedAccount.getCustomer().getPhone(),
                        savedAccount.getCustomer().getAddress()
                ),
                new BankResponseDTO(
                        savedAccount.getBank().getId(),
                        savedAccount.getBank().getName(),
                        savedAccount.getBank().getIfsc_code(),
                        savedAccount.getBank().getAddress()
                )
        );
    }

    @Override
    public AccountResponseDTO withdrawAmount(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(amount));
        Account savedAccount = accountRepository.save(account);

        return new AccountResponseDTO(
                savedAccount.getId(),
                savedAccount.getAccountNumber(),
                savedAccount.getAccountType(),
                savedAccount.getBalance(),
                savedAccount.getStatus(),
                savedAccount.getCreatedAt(),
                new CustomerResponseDTO(
                        savedAccount.getCustomer().getCustomerId(),
                        savedAccount.getCustomer().getName(),
                        savedAccount.getCustomer().getEmail(),
                        savedAccount.getCustomer().getPhone(),
                        savedAccount.getCustomer().getAddress()
                ),
                new BankResponseDTO(
                        savedAccount.getBank().getId(),
                        savedAccount.getBank().getName(),
                        savedAccount.getBank().getIfsc_code(),
                        savedAccount.getBank().getAddress()
                )
        );
    }

    @Override
    public AccountResponseDTO transferAmount(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("Source account not found"));

        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("Target account not found"));

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        Account updatedToAccount = accountRepository.save(toAccount);


        return new AccountResponseDTO(
                updatedToAccount.getId(),
                updatedToAccount.getAccountNumber(),
                updatedToAccount.getAccountType(),
                updatedToAccount.getBalance(),
                updatedToAccount.getStatus(),
                updatedToAccount.getCreatedAt(),
                new CustomerResponseDTO(
                        updatedToAccount.getCustomer().getCustomerId(),
                        updatedToAccount.getCustomer().getName(),
                        updatedToAccount.getCustomer().getEmail(),
                        updatedToAccount.getCustomer().getPhone(),
                        updatedToAccount.getCustomer().getAddress()
                ),
                new BankResponseDTO(
                        updatedToAccount.getBank().getId(),
                        updatedToAccount.getBank().getName(),
                        updatedToAccount.getBank().getIfsc_code(),
                        updatedToAccount.getBank().getAddress()
                )
        );
    }






}
