package com.example.demo.service.impl;

import com.example.demo.entity.FinancialProfile;
import com.example.demo.entity.User;
import com.example.demo.repository.FinancialProfileRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.FinancialProfileService;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FinancialProfileServiceImpl implements FinancialProfileService {

    private final FinancialProfileRepository profileRepository;
    private final UserRepository userRepository;

    public FinancialProfileServiceImpl(FinancialProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public FinancialProfile createOrUpdate(FinancialProfile profile) {
        // Validation per requirements
        if (profile.getMonthlyIncome() <= 0) throw new BadRequestException("monthlyIncome must be > 0");
        if (profile.getCreditScore() < 300 || profile.getCreditScore() > 900) throw new BadRequestException("creditScore must be 300-900");

        // Fetch User to ensure they exist in DB
        User user = userRepository.findById(profile.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Check if profile exists for this user
        return profileRepository.findByUserId(user.getId())
            .map(existing -> {
                existing.setMonthlyIncome(profile.getMonthlyIncome());
                existing.setMonthlyExpenses(profile.getMonthlyExpenses());
                existing.setExistingLoanEmi(profile.getExistingLoanEmi());
                existing.setCreditScore(profile.getCreditScore());
                existing.setSavingsBalance(profile.getSavingsBalance());
                return profileRepository.save(existing);
            })
            .orElseGet(() -> {
                profile.setUser(user); // Link the detached user object
                return profileRepository.save(profile);
            });
    }

    @Override
    public FinancialProfile getByUserId(Long userId) {
        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
    }
}