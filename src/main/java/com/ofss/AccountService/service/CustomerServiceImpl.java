package com.ofss.AccountService.service;

import com.ofss.AccountService.DTO.AccountResponseDTO;
import com.ofss.AccountService.DTO.BankResponseDTO;
import com.ofss.AccountService.DTO.CustomerResponseDTO;
import com.ofss.AccountService.models.Customer;
import com.ofss.AccountService.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public List<Customer> getAllCustomers() {
        return (List<Customer>) customerRepository.findAll();
    }

    @Override
    public Object getAccountByAccountNumber(String accountNumber) {
        return customerRepository.findByAccountNumber(accountNumber);
    }

    @Override
    public Optional<CustomerResponseDTO> getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(customer -> new CustomerResponseDTO(
                        customer.getCustomerId(),
                        customer.getName(),
                        customer.getEmail(),
                        customer.getPhone(),
                        customer.getAddress()
                ));
    }

    @Override
    public CustomerResponseDTO createCustomer(CustomerResponseDTO customerDTO) {
        Customer customer = new Customer(
                customerDTO.getName(),
                customerDTO.getAddress(),
                customerDTO.getEmail(),
                customerDTO.getPhone()
        );
        Customer savedCustomer = customerRepository.save(customer);

        return new CustomerResponseDTO(
                savedCustomer.getCustomerId(),
                savedCustomer.getName(),
                savedCustomer.getEmail(),
                savedCustomer.getPhone(),
                savedCustomer.getAddress()
        );
    }

    @Override
    public CustomerResponseDTO updateCustomer(Long customerId, CustomerResponseDTO customerDTO) {
        return customerRepository.findById(customerId)
                .map(existingCustomer -> {
                    existingCustomer.setName(customerDTO.getName());
                    existingCustomer.setEmail(customerDTO.getEmail());
                    existingCustomer.setPhone(customerDTO.getPhone());
                    existingCustomer.setAddress(customerDTO.getAddress());
                    Customer updatedCustomer = customerRepository.save(existingCustomer);
                    return new CustomerResponseDTO(
                            updatedCustomer.getCustomerId(),
                            updatedCustomer.getName(),
                            updatedCustomer.getEmail(),
                            updatedCustomer.getPhone(),
                            updatedCustomer.getAddress()
                    );
                })
                .orElse(null);
    }

    @Override
    public CustomerResponseDTO patchCustomer(Long customerId, CustomerResponseDTO customerDTO) {
        return customerRepository.findById(customerId)
                .map(existingCustomer -> {
                    if (customerDTO.getName() != null) {
                        existingCustomer.setName(customerDTO.getName());
                    }
                    if (customerDTO.getEmail() != null) {
                        existingCustomer.setEmail(customerDTO.getEmail());
                    }
                    if (customerDTO.getPhone() != null) {
                        existingCustomer.setPhone(customerDTO.getPhone());
                    }
                    if (customerDTO.getAddress() != null) {
                        existingCustomer.setAddress(customerDTO.getAddress());
                    }
                    Customer updatedCustomer = customerRepository.save(existingCustomer);
                    return new CustomerResponseDTO(
                            updatedCustomer.getCustomerId(),
                            updatedCustomer.getName(),
                            updatedCustomer.getEmail(),
                            updatedCustomer.getPhone(),
                            updatedCustomer.getAddress()
                    );
                })
                .orElse(null);
    }


}