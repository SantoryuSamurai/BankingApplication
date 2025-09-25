package com.ofss.AccountService.service;

import com.ofss.AccountService.DTO.CustomerResponseDTO;
import com.ofss.AccountService.models.Customer;
import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<Customer> getAllCustomers();

    Object getAccountByAccountNumber(String accountNumber);

    Optional<CustomerResponseDTO> getCustomerById(Long id);

    CustomerResponseDTO createCustomer(CustomerResponseDTO customerDTO);

    CustomerResponseDTO updateCustomer(Long customerId, CustomerResponseDTO customerDTO);

    CustomerResponseDTO patchCustomer(Long customerId, CustomerResponseDTO customerDTO);
}
