package com.ofss.AccountService.service;

import com.ofss.AccountService.DTO.AccountResponseDTO;
import com.ofss.AccountService.models.Account;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AccountService {
    List<AccountResponseDTO> getAllAccounts(); //
}
