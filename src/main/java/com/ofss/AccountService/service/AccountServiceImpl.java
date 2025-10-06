package com.ofss.AccountService.service;

import com.ofss.AccountService.DTO.AccountPostDTO;
import com.ofss.AccountService.DTO.AccountResponseDTO;
import com.ofss.AccountService.DTO.BankResponseDTO;
import com.ofss.AccountService.DTO.CustomerResponseDTO;
import com.ofss.AccountService.models.Account;
import com.ofss.AccountService.models.Bank;
import com.ofss.AccountService.models.Customer;
import com.ofss.AccountService.models.Transaction;
import com.ofss.AccountService.repository.AccountRepository;
import com.ofss.AccountService.repository.BankRepository;
import com.ofss.AccountService.repository.CustomerRepository;
import com.ofss.AccountService.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final BankRepository bankRepository;
    private final TransactionRepository transactionRepository; // Added

    @Override
    public AccountResponseDTO createAccount(AccountPostDTO accountPostDTO) {
        Customer customer = customerRepository.findById(accountPostDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Bank bank = bankRepository.findById(accountPostDTO.getBankId())
                .orElseThrow(() -> new RuntimeException("Bank not found"));

        Account account = new Account();
        // Generate account number based on bank name
        String bankCode = generateBankCode(bank.getName());
        String accountNumber;
        do {
            accountNumber = bankCode + generateRandomDigits(6);
        } while (accountRepository.existsByAccountNumber(accountNumber));
        account.setAccountNumber(accountNumber);

        account.setAccountType(accountPostDTO.getAccountType());
        account.setBalance(accountPostDTO.getBalance());
        account.setStatus(accountPostDTO.getStatus());
        account.setCustomer(customer);
        account.setBank(bank);

        Account saved = accountRepository.save(account);

        return mapToAccountResponseDTO(saved);
    }

    private String generateBankCode(String bankName) {
        String shortCode = bankName.replaceAll("\\s+", "").toUpperCase();

        if (shortCode.length() < 4) {
            return String.format("%-4s", shortCode).replace(' ', '0');
        } else {
            return shortCode.substring(0, 4);
        }
    }


    private String generateRandomDigits(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

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
    public AccountResponseDTO updateAccount(Long accountId, AccountPostDTO accountPostDTO) {
        return accountRepository.findById(accountId)
                .map(existingAccount -> {
                    Customer customer = customerRepository.findById(accountPostDTO.getCustomerId())
                            .orElseThrow(() -> new RuntimeException("Customer not found"));

                    Bank bank = bankRepository.findById(accountPostDTO.getBankId())
                            .orElseThrow(() -> new RuntimeException("Bank not found"));

//                    existingAccount.setAccountNumber(accountPostDTO.getAccountNumber());
                    existingAccount.setAccountType(accountPostDTO.getAccountType());
                    existingAccount.setBalance(accountPostDTO.getBalance());
                    existingAccount.setStatus(accountPostDTO.getStatus());
                    existingAccount.setCustomer(customer);
                    existingAccount.setBank(bank); // when new bank is applied

                    // If the bank ID is changed, update the account number as well
                    String newBankPrefix = generateBankCode(bank.getName());
                    String newAccountNumber;
                    do {
                        newAccountNumber = newBankPrefix + generateRandomDigits(6);
                    } while (accountRepository.existsByAccountNumber(newAccountNumber));
                    existingAccount.setAccountNumber(newAccountNumber);


                    Account savedAccount = accountRepository.save(existingAccount);

                    return mapToAccountResponseDTO(savedAccount);
                })
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @Override
    public AccountResponseDTO patchAccount(Long accountId, AccountPostDTO accountPostDTO) {
        return accountRepository.findById(accountId)
                .map(existingAccount -> {
//                    if (accountPostDTO.getAccountNumber() != null) {
//                        existingAccount.setAccountNumber(accountPostDTO.getAccountNumber());
//                    }
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
                    if (accountPostDTO.getBankId() != null &&
                            !existingAccount.getBank().getId().equals(accountPostDTO.getBankId())) {
                        Bank newBank = bankRepository.findById(accountPostDTO.getBankId())
                                .orElseThrow(() -> new RuntimeException("Bank not found"));
                        existingAccount.setBank(newBank);

                        String newBankPrefix = generateBankCode(newBank.getName());
                        String newAccountNumber;
                        do {
                            newAccountNumber = newBankPrefix + generateRandomDigits(6);
                        } while (accountRepository.existsByAccountNumber(newAccountNumber));
                        existingAccount.setAccountNumber(newAccountNumber);
                    }

                    Account updatedAccount = accountRepository.save(existingAccount);

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

        // Save transaction record
        recordTransaction(amount, "DEPOSIT", null, account, account.getBank(), "Deposit to account");

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

        // Save transaction record
        recordTransaction(amount, "WITHDRAW", account, null, account.getBank(), "Withdrawal from account");

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

        // Save transaction record
        recordTransaction(amount, "TRANSFER", fromAccount, toAccount, fromAccount.getBank(), "Transfer between accounts");

        return mapToAccountResponseDTO(updatedToAccount);
    }

    private void recordTransaction(BigDecimal amount, String type, Account sourceAccount, Account targetAccount, Bank bank, String remarks) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setSourceAccount(sourceAccount);
        transaction.setTargetAccount(targetAccount);
        transaction.setBank(bank);
        transaction.setRemarks(remarks);

        transactionRepository.save(transaction);
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
