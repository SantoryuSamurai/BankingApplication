package com.ofss.AccountService.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDTO {
    private Long id;
    private BigDecimal amount;
    private String type;
    private LocalDateTime timestamp;
    private String remarks;
    private AccountResponseDTO sourceAccount;
    private AccountResponseDTO targetAccount;
    private BankResponseDTO bank;
}
