package com.ofss.AccountService.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankResponseDTO {
    private Long id;
    private String name;
    private String ifscCode;
    private String address;
}
