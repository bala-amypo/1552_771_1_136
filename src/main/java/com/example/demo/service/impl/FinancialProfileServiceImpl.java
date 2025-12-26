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

    private final FinancialProfileRepository fpRepo;
    private final UserRepository userRepo;

    public FinancialProfileServiceImpl(FinancialProfileRepository fpRepo, UserRepository userRepo) {
        this.fpRepo = fpRepo;
        this.userRepo = userRepo;
    }

    @Override
    public FinancialProfile createOrUpdate(FinancialProfile profile) {
        if (profile.getUser() == null || profile.getUser().getId() == null) {
            throw new ResourceNotFoundException("User not found");
        }
        User user = userRepo.findById(profile.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (profile.getCreditScore() < 300 || profile.getCreditScore() > 900) {
            throw new BadRequestException("creditScore invalid");
        }
        if (profile.getMonthlyIncome() <= 0) {
            throw new BadRequestException("monthlyIncome must be > 0");
        }

        Optional<FinancialProfile> existing = fpRepo.findByUserId(user.getId());
        if (existing.isPresent()) {
            throw new BadRequestException("Financial profile already exists");
        }

        profile.setUser(user);
        return fpRepo.save(profile);
    }

    @Override
    public FinancialProfile getByUserId(Long userId) {
        return fpRepo.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Financial profile not found"));
    }
}
