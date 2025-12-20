package com.example.demo.service.impl;

import com.example.demo.entity.FinancialProfile;
import com.example.demo.entity.User;
import com.example.demo.repository.FinancialProfileRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.FinancialProfileService;
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
        User user = userRepository.findById(profile.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        profile.setUser(user);
        profile.setLastUpdatedAt(java.time.LocalDateTime.now());

        // Check for existing profile
        return profileRepository.findByUserId(user.getId())
                .map(existingProfile -> {
                    existingProfile.setMonthlyIncome(profile.getMonthlyIncome());
                    existingProfile.setMonthlyExpenses(profile.getMonthlyExpenses());
                    existingProfile.setExistingLoanEmi(profile.getExistingLoanEmi());
                    existingProfile.setCreditScore(profile.getCreditScore());
                    existingProfile.setSavingsBalance(profile.getSavingsBalance());
                    existingProfile.setLastUpdatedAt(java.time.LocalDateTime.now());
                    return profileRepository.save(existingProfile);
                })
                .orElseGet(() -> profileRepository.save(profile));
    }

    @Override
    public FinancialProfile getByUserId(Long userId) {
        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Financial profile not found"));
    }
}
