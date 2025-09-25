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
        return accounts.stream()
                .map(this::mapToAccountResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AccountResponseDTO> getAccountById(Long id) {
        return accountRepository.findById(id).map(this::mapToAccountResponseDTO);
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
        return mapToAccountResponseDTO(saved);
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

                    return mapToAccountResponseDTO(savedAccount);
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

                    return mapToAccountResponseDTO(updatedAccount);
                })
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @Override
    public AccountResponseDTO depositAmount(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setBalance(account.getBalance().add(amount));
        Account savedAccount = accountRepository.save(account);

        return mapToAccountResponseDTO(savedAccount);
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

        return mapToAccountResponseDTO(savedAccount);
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

        return mapToAccountResponseDTO(updatedToAccount);
    }

    private AccountResponseDTO mapToAccountResponseDTO(Account account) {
        return new AccountResponseDTO(
                account.getId(),
                account.getAccountNumber(),
                account.getAccountType(),
                account.getBalance(),
                account.getStatus(),
                account.getCreatedAt(),
                new CustomerResponseDTO(
                        account.getCustomer().getCustomerId(),
                        account.getCustomer().getName(),
                        account.getCustomer().getEmail(),
                        account.getCustomer().getPhone(),
                        account.getCustomer().getAddress()
                ),
                new BankResponseDTO(
                        account.getBank().getId(),
                        account.getBank().getName(),
                        account.getBank().getIfsc_code(),
                        account.getBank().getAddress()
                )
        );
    }

}
