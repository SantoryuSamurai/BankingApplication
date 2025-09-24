package com.ofss.AccountService.service;

import com.ofss.AccountService.DTO.AccountCreateDTO;
import com.ofss.AccountService.DTO.AccountResponseDTO;
import com.ofss.AccountService.models.Account;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<AccountResponseDTO> getAllAccounts(); //
    Optional<AccountResponseDTO> getAccountById(Long id);


}
