package com.ofss.AccountService.DTO;

import com.ofss.AccountService.models.Bank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDTO {
    private Long id;
    private String accountNumber;
//    private String bankName;
    private String accountType;
    private BigDecimal balance;
    private String status;
    private LocalDateTime createdAt;
    private CustomerResponseDTO customer;
    private BankResponseDTO bank;


}
