package com.ofss.AccountService.service;

import com.ofss.AccountService.models.Bank;
import com.ofss.AccountService.repository.BankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

    private final BankRepository bankRepository;

    @Override
    public List<Bank> getAllBanks() {
        return (List<Bank>) bankRepository.findAll();
    }
}
