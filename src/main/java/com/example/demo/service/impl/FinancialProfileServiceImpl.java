package com.example.demo.service.impl;

import com.example.demo.entity.FinancialProfile;
import com.example.demo.entity.User;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.FinancialProfileRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.FinancialProfileService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FinancialProfileServiceImpl implements FinancialProfileService {

    private final FinancialProfileRepository profileRepository;
    private final UserRepository userRepository;

    public FinancialProfileServiceImpl(FinancialProfileRepository profileRepository,
                                       UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    @Override
    public FinancialProfile createOrUpdate(FinancialProfile profile) {

        if (profile.getMonthlyIncome() == null || profile.getMonthlyIncome() <= 0) {
            throw new BadRequestException("Monthly income must be greater than zero");
        }

        if (profile.getCreditScore() < 300 || profile.getCreditScore() > 900) {
            throw new BadRequestException("creditScore");
        }

        Long userId = profile.getUser().getId();
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        profile.setUser(user);

        profileRepository.findByUserId(userId).ifPresent(existing -> {
            if (profile.getId() == null) {
                throw new BadRequestException("Financial profile already exists");
            }
        });

        profile.setLastUpdatedAt(LocalDateTime.now());
        return profileRepository.save(profile);
    }

    @Override
    public FinancialProfile getByUserId(Long userId) {
        return profileRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Financial profile not found"));
    }
}
