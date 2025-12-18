package com.example.demo.service.impl;

import com.example.demo.entity.FinancialProfile;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.FinancialProfileRepository;
import com.example.demo.service.FinancialProfileService;

public class FinancialProfileServiceImpl implements FinancialProfileService {

    private final FinancialProfileRepository profileRepository;

    // ✅ Constructor injection
    public FinancialProfileServiceImpl(FinancialProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public FinancialProfile createOrUpdateProfile(FinancialProfile profile) {
        if (profile.getId() == null && profileRepository.findByUserId(profile.getUser().getId()).isPresent()) {
            throw new BadRequestException("Financial profile already exists");
        }
        return profileRepository.save(profile);
    }

    @Override
    public FinancialProfile getProfileByUser(Long userId) {
        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Financial profile not found"));
    }
}
