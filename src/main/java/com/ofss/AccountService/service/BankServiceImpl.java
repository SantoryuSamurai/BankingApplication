package com.ofss.AccountService.service;

import com.ofss.AccountService.DTO.BankResponseDTO;
import com.ofss.AccountService.models.Bank;
import com.ofss.AccountService.repository.BankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    @Override
    public Optional<BankResponseDTO> getBankById(Long bankId) {
        return bankRepository.findById(bankId)
                .map(bank -> new BankResponseDTO(
                        bank.getId(),
                        bank.getName(),
                        bank.getIfsc_code(),
                        bank.getAddress()
                ));
    }

    @Override
    public BankResponseDTO createBank(BankResponseDTO bankDTO) {
        Bank bank = new Bank();
        bank.setName(bankDTO.getName());
        bank.setIfsc_code(bankDTO.getIfscCode());
        bank.setAddress(bankDTO.getAddress());

        Bank savedBank = bankRepository.save(bank);

        return new BankResponseDTO(
                savedBank.getId(),
                savedBank.getName(),
                savedBank.getIfsc_code(),
                savedBank.getAddress()
        );
    }

    @Override
    public BankResponseDTO updateBank(Long bankId, BankResponseDTO bankDTO) {
        return bankRepository.findById(bankId)
                .map(existingBank -> {
                    existingBank.setName(bankDTO.getName());
                    existingBank.setIfsc_code(bankDTO.getIfscCode());
                    existingBank.setAddress(bankDTO.getAddress());
                    Bank updatedBank = bankRepository.save(existingBank);
                    return new BankResponseDTO(
                            updatedBank.getId(),
                            updatedBank.getName(),
                            updatedBank.getIfsc_code(),
                            updatedBank.getAddress()
                    );
                })
                .orElse(null);  // Or throw exception if preferred
    }

    @Override
    public BankResponseDTO patchBank(Long bankId, BankResponseDTO bankDTO) {
        return bankRepository.findById(bankId)
                .map(existingBank -> {
                    if (bankDTO.getName() != null) {
                        existingBank.setName(bankDTO.getName());
                    }
                    if (bankDTO.getIfscCode() != null) {
                        existingBank.setIfsc_code(bankDTO.getIfscCode());
                    }
                    if (bankDTO.getAddress() != null) {
                        existingBank.setAddress(bankDTO.getAddress());
                    }
                    Bank updatedBank = bankRepository.save(existingBank);
                    return new BankResponseDTO(
                            updatedBank.getId(),
                            updatedBank.getName(),
                            updatedBank.getIfsc_code(),
                            updatedBank.getAddress()
                    );
                })
                .orElse(null);
    }



}
