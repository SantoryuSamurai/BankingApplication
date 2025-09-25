package com.ofss.AccountService.DTO;

import com.ofss.AccountService.models.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountPostDTO {
    private String accountNumber;
    private String accountType;
    private BigDecimal balance;
    private AccountStatus status;
    private Long customerId;
    private Long bankId;
}
