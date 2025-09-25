package com.ofss.AccountService.service;

import com.ofss.AccountService.DTO.AccountPostDTO;
import com.ofss.AccountService.DTO.AccountResponseDTO;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<AccountResponseDTO> getAllAccounts(); //
    Optional<AccountResponseDTO> getAccountById(Long id);

    AccountResponseDTO createAccount(AccountPostDTO accountPostDTO);

    AccountResponseDTO updateAccount(Long accountId, AccountPostDTO accountPostDTO);

    AccountResponseDTO patchAccount(Long accountId, AccountPostDTO accountPostDTO);

}
