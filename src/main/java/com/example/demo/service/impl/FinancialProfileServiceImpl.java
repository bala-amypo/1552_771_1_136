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
    private final FinancialProfileRepository profileRepository;
    private final UserRepository userRepository;

    public FinancialProfileServiceImpl(FinancialProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    @Override
    public FinancialProfile createOrUpdate(FinancialProfile profile) {
        // Validate user exists
        userRepository.findById(profile.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Validate Credit Score range
        if (profile.getCreditScore() < 300 || profile.getCreditScore() > 900) {
            throw new BadRequestException("Invalid creditScore range");
        }

        // Enforce one profile per user (for new creations)
        if (profile.getId() == null && profileRepository.findByUserId(profile.getUser().getId()).isPresent()) {
            throw new BadRequestException("Financial profile already exists");
        }

        return profileRepository.save(profile);
    }

    @Override
    public FinancialProfile getByUserId(Long userId) {
        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Financial profile not found"));
    }
}