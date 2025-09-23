package com.ofss.AccountService.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDTO {
    private Long customerId;
    private String name;
    private String email;
    private String phone;
    private String address; // optional if you want to include
}
