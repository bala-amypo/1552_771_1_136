package com.example.demo.service.impl;

import com.example.demo.entity.FinancialProfile;
import com.example.demo.repository.FinancialProfileRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.FinancialProfileService;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class FinancialProfileServiceImpl implements FinancialProfileService {

    private final FinancialProfileRepository profileRepository;
    private final UserRepository userRepository;

    public FinancialProfileServiceImpl(FinancialProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    @Override
    public FinancialProfile createOrUpdate(FinancialProfile profile) {
        // 1. Validation Logic
        if (profile.getMonthlyIncome() <= 0) {
            throw new BadRequestException("monthlyIncome must be greater than 0");
        }
        if (profile.getCreditScore() < 300 || profile.getCreditScore() > 900) {
            throw new BadRequestException("creditScore must be between 300 and 900");
        }
        if (profile.getSavingsBalance() < 0) {
            throw new BadRequestException("savingsBalance cannot be negative");
        }

        // 2. Check if User exists
        Long userId = profile.getUser().getId();
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }

        // 3. Enforce one profile per user logic
        return profileRepository.findByUserId(userId)
            .map(existingProfile -> {
                // Update logic
                existingProfile.setMonthlyIncome(profile.getMonthlyIncome());
                existingProfile.setMonthlyExpenses(profile.getMonthlyExpenses());
                existingProfile.setExistingLoanEmi(profile.getExistingLoanEmi());
                existingProfile.setCreditScore(profile.getCreditScore());
                existingProfile.setSavingsBalance(profile.getSavingsBalance());
                return profileRepository.save(existingProfile);
            })
            .orElseGet(() -> profileRepository.save(profile)); // Create logic
    }

    @Override
    public FinancialProfile getByUserId(Long userId) {
        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Financial profile not found for user"));
    }
}