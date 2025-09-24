package com.ofss.AccountService.controllers;

import com.ofss.AccountService.models.Customer;
import com.ofss.AccountService.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @RequestMapping("/customers")
    public ResponseEntity<?> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        if (customers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Customers Found");
        }
        return ResponseEntity.ok(customers);
    }
}
