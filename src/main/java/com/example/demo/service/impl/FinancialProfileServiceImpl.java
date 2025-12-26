package com.example.demo.service.impl;

import com.example.demo.entity.FinancialProfile;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.FinancialProfileRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.FinancialProfileService;
import org.springframework.stereotype.Service;

@Service
public class FinancialProfileServiceImpl implements FinancialProfileService {

    private final FinancialProfileRepository financialProfileRepository;
    private final UserRepository userRepository;

    public FinancialProfileServiceImpl(FinancialProfileRepository financialProfileRepository, UserRepository userRepository) {
        this.financialProfileRepository = financialProfileRepository;
        this.userRepository = userRepository;
    }

    @Override
    public FinancialProfile createOrUpdate(FinancialProfile profile) {
        if (profile.getCreditScore() < 300 || profile.getCreditScore() > 900) {
            throw new BadRequestException("Invalid creditScore"); [cite: 46, 47]
        }

        return financialProfileRepository.findByUserId(profile.getUser().getId())
                .map(existing -> {
                    existing.setMonthlyIncome(profile.getMonthlyIncome());
                    existing.setMonthlyExpenses(profile.getMonthlyExpenses());
                    existing.setExistingLoanEmi(profile.getExistingLoanEmi());
                    existing.setCreditScore(profile.getCreditScore());
                    existing.setSavingsBalance(profile.getSavingsBalance());
                    return financialProfileRepository.save(existing); [cite: 79]
                })
                .orElseGet(() -> financialProfileRepository.save(profile)); [cite: 45]
    }

    @Override
    public FinancialProfile getByUserId(Long userId) {
        return financialProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Financial profile not found")); [cite: 125]
    }
}