package com.example.demo.service.impl;

import com.example.demo.entity.FinancialProfile;
import com.example.demo.entity.User;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.FinancialProfileRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.FinancialProfileService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FinancialProfileServiceImpl implements FinancialProfileService {

    private final FinancialProfileRepository profileRepository;
    private final UserRepository userRepository;

    // Constructor Injection
    public FinancialProfileServiceImpl(FinancialProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    @Override
    public FinancialProfile createOrUpdate(FinancialProfile profile) {
        // 1. Verify user exists
        User user = userRepository.findById(profile.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // 2. Validate Credit Score Range (300-900)
        if (profile.getCreditScore() < 300 || profile.getCreditScore() > 900) {
            throw new BadRequestException("creditScore");
        }

        // 3. Check for monthly income > 0
        if (profile.getMonthlyIncome() <= 0) {
            throw new BadRequestException("Monthly income must be greater than zero");
        }

        // 4. Enforce one profile per user (only for new creations)
        if (profile.getId() == null) {
            Optional<FinancialProfile> existing = profileRepository.findByUserId(user.getId());
            if (existing.isPresent()) {
                throw new BadRequestException("Financial profile already exists");
            }
        }

        profile.setUser(user);
        return profileRepository.save(profile);
    }

    @Override
    public FinancialProfile getByUserId(Long userId) {
        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Financial profile not found"));
    }
}