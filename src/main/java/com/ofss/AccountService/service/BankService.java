package com.ofss.AccountService.service;

import com.ofss.AccountService.DTO.BankResponseDTO;
import com.ofss.AccountService.models.Bank;
import java.util.List;
import java.util.Optional;

public interface BankService {
    List<BankResponseDTO> getAllBanks();

    Optional<BankResponseDTO> getBankById(Long bankId);

    BankResponseDTO createBank(BankResponseDTO bankDTO);

    BankResponseDTO updateBank(Long bankId, BankResponseDTO bankDTO);

    BankResponseDTO patchBank(Long bankId, BankResponseDTO bankDTO);
}
