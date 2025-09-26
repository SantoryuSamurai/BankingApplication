package com.ofss.AccountService.repository;

import com.ofss.AccountService.models.Transaction;
import com.ofss.AccountService.models.Account;
import com.ofss.AccountService.models.Bank;
import com.ofss.AccountService.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByBank(Bank bank);

    @Query("SELECT t FROM Transaction t WHERE t.sourceAccount = :account OR t.targetAccount = :account")
    List<Transaction> findByAccount(Account account);

    @Query("SELECT t FROM Transaction t WHERE t.sourceAccount.customer = :customer OR t.targetAccount.customer = :customer")
    List<Transaction> findByCustomer(Customer customer);
}
