package com.ofss.AccountService.service;

import com.ofss.AccountService.DTO.BankResponseDTO;
import com.ofss.AccountService.models.Bank;
import java.util.List;

public interface BankService {
    List<BankResponseDTO> getAllBanks();

}
