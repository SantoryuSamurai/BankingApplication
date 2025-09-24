package com.ofss.AccountService.controllers;

import com.ofss.AccountService.DTO.BankResponseDTO;
import com.ofss.AccountService.models.Bank;
import com.ofss.AccountService.service.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BankController {
    private final BankService bankService;

    @RequestMapping("/banks")
    public ResponseEntity<?> getAllBanks() {
        List<BankResponseDTO> banks = bankService.getAllBanks();
        if (banks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Banks Found");
        }
        return ResponseEntity.ok(banks);
    }
}
