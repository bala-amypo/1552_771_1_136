package com.example.demo.service.impl;

import org.springframework.stereotype.Service;
import com.example.demo.service.FinancialProfileService;
import com.example.demo.entity.FinancialProfile;
import com.example.demo.repository.FinancialProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FinancialProfileServiceImpl implements FinancialProfileService {

    private final FinancialProfileRepository profileRepository;

    @Override
    public FinancialProfile createProfile(FinancialProfile profile) {
        return profileRepository.save(profile);
    }

    @Override
    public FinancialProfile getByUserId(Long userId) {
        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    @Override
    public FinancialProfile updateProfile(FinancialProfile profile) {
        return profileRepository.save(profile);
    }
}
