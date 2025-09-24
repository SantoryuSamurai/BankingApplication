package com.ofss.AccountService.service;

import com.ofss.AccountService.DTO.BankResponseDTO;
import com.ofss.AccountService.models.Bank;
import com.ofss.AccountService.repository.BankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

    private final BankRepository bankRepository;

    @Override
    public List<BankResponseDTO> getAllBanks() {
        List<Bank> banks = (List<Bank>) bankRepository.findAll();

        return banks.stream().map(bank -> new BankResponseDTO(
                bank.getId(),
                bank.getName(),
                bank.getIfsc_code(),
                bank.getAddress()
        )).collect(Collectors.toList());
    }
}
