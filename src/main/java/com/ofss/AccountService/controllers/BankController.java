package com.ofss.AccountService.controllers;

import com.ofss.AccountService.DTO.BankResponseDTO;
import com.ofss.AccountService.models.Bank;
import com.ofss.AccountService.service.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class BankController {
    private final BankService bankService;

    @GetMapping("/banks")
    public ResponseEntity<?> getAllBanks() {
        List<BankResponseDTO> banks = bankService.getAllBanks();
        if (banks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Banks Found");
        }
        return ResponseEntity.ok(banks);
    }

    @GetMapping("/banks/id/{bankId}")
    public ResponseEntity<?> getBankById(@PathVariable Long bankId) {
        Optional<BankResponseDTO> bankOpt = bankService.getBankById(bankId);
        if (bankOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bank not found");
        }
        return ResponseEntity.ok(bankOpt.get());
    }


    @PostMapping("/banks")
    public ResponseEntity<?> createBank(@RequestBody BankResponseDTO bankDTO) {
        BankResponseDTO createdBank = bankService.createBank(bankDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBank);
    }


    @PutMapping("/banks/id/{bankId}")
    public ResponseEntity<?> updateBank(@PathVariable Long bankId, @RequestBody BankResponseDTO bankDTO) {
        BankResponseDTO updatedBank = bankService.updateBank(bankId, bankDTO);
        if (updatedBank == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bank not found");
        }
        return ResponseEntity.ok(updatedBank);
    }

    @PatchMapping("/banks/id/{bankId}")
    public ResponseEntity<?> patchBank(@PathVariable Long bankId, @RequestBody BankResponseDTO bankDTO) {
        BankResponseDTO updatedBank = bankService.patchBank(bankId, bankDTO);
        if (updatedBank == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bank not found");
        }
        return ResponseEntity.ok(updatedBank);
    }


}
