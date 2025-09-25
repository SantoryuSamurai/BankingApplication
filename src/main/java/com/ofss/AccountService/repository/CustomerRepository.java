package com.ofss.AccountService.repository;

import com.ofss.AccountService.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT c FROM Customer c JOIN c.accounts a WHERE a.accountNumber = :accountNumber")
    Customer findByAccountNumber(@Param("accountNumber") String accountNumber);
}
