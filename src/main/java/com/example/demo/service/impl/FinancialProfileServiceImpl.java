package com.example.demo.service.impl;

import com.example.demo.entity.FinancialProfile;
import com.example.demo.entity.User;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.FinancialProfileRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.FinancialProfileService;

import java.time.LocalDateTime;
import java.util.Optional;

public class FinancialProfileServiceImpl implements FinancialProfileService {

    private final FinancialProfileRepository financialProfileRepository;
    private final UserRepository userRepository;

    public FinancialProfileServiceImpl(FinancialProfileRepository financialProfileRepository,
                                       UserRepository userRepository) {
        this.financialProfileRepository = financialProfileRepository;
        this.userRepository = userRepository;
    }

    @Override
    public FinancialProfile createOrUpdate(FinancialProfile profile) {
        Long userId = profile.getUser().getId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (profile.getCreditScore() < 300 || profile.getCreditScore() > 900) {
            throw new BadRequestException("creditScore");
        }

        Optional<FinancialProfile> existing = financialProfileRepository.findByUserId(userId);

        if (existing.isPresent()) {
            FinancialProfile toUpdate = existing.get();
            toUpdate.setMonthlyIncome(profile.getMonthlyIncome());
            toUpdate.setMonthlyExpenses(profile.getMonthlyExpenses());
            toUpdate.setExistingLoanEmi(profile.getExistingLoanEmi());
            toUpdate.setCreditScore(profile.getCreditScore());
            toUpdate.setSavingsBalance(profile.getSavingsBalance());
            toUpdate.setLastUpdatedAt(LocalDateTime.now());
            return financialProfileRepository.save(toUpdate);
        } else {
            profile.setUser(user);
            profile.setLastUpdatedAt(LocalDateTime.now());
            return financialProfileRepository.save(profile);
        }
    }

    @Override
    public FinancialProfile getByUserId(Long userId) {
        return financialProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Financial profile not found"));
    }
}
