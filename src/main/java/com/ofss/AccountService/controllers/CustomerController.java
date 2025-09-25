package com.ofss.AccountService.controllers;

import com.ofss.AccountService.DTO.AccountResponseDTO;
import com.ofss.AccountService.DTO.CustomerResponseDTO;
import com.ofss.AccountService.models.Customer;
import com.ofss.AccountService.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping ("/customers")
    public ResponseEntity<?> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        if (customers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Customers Found");
        }
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/customers/accountNumber/{accountNumber}")
    public ResponseEntity<?> getAccountByAccountNumber(@PathVariable String accountNumber){
        return ResponseEntity.ok(customerService.getAccountByAccountNumber(accountNumber));

    }

    @GetMapping("/customers/id/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long id) {
        Optional<CustomerResponseDTO> customers = customerService.getCustomerById(id);
        if (customers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }
        return ResponseEntity.ok(customers.get());
    }

    @PostMapping("/customers")
    public ResponseEntity<?> createCustomer(@RequestBody CustomerResponseDTO customerDTO) {
        CustomerResponseDTO savedCustomer = customerService.createCustomer(customerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
    }

    @PutMapping("/customers/id/{customerId}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long customerId, @RequestBody CustomerResponseDTO customerDTO) {
        CustomerResponseDTO updatedCustomer = customerService.updateCustomer(customerId, customerDTO);
        if (updatedCustomer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }
        return ResponseEntity.ok(updatedCustomer);
    }

    @PatchMapping("/customers/id/{customerId}")
    public ResponseEntity<?> patchCustomer(@PathVariable Long customerId, @RequestBody CustomerResponseDTO customerDTO) {
        CustomerResponseDTO updatedCustomer = customerService.patchCustomer(customerId, customerDTO);
        if (updatedCustomer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }
        return ResponseEntity.ok(updatedCustomer);
    }
}
