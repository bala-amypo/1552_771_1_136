// src/main/java/com/example/demo/service/impl/FinancialProfileServiceImpl.java
package com.example.demo.service.impl;

import com.example.demo.entity.FinancialProfile;
import com.example.demo.entity.User;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.FinancialProfileRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.FinancialProfileService;

import java.util.Objects;
import java.util.Optional;

public class FinancialProfileServiceImpl implements FinancialProfileService {
    private final FinancialProfileRepository fpRepo;
    private final UserRepository userRepo;

    public FinancialProfileServiceImpl(FinancialProfileRepository fpRepo, UserRepository userRepo) {
        this.fpRepo = fpRepo; this.userRepo = userRepo;
    }

    @Override
    public FinancialProfile createOrUpdate(FinancialProfile profile) {
        Objects.requireNonNull(fpRepo, "FinancialProfileRepository required");
        Objects.requireNonNull(userRepo, "UserRepository required");

        if (profile.getUser() == null || profile.getUser().getId() == null) {
            throw new ResourceNotFoundException("User not found");
        }
        User user = userRepo.findById(profile.getUser().getId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Integer score = profile.getCreditScore();
        if (score == null || score < 300 || score > 900) {
            throw new BadRequestException("Invalid creditScore");
        }
        if (profile.getMonthlyIncome() == null || profile.getMonthlyIncome() <= 0.0) {
            throw new BadRequestException("monthlyIncome must be > 0");
        }
        if (profile.getSavingsBalance() == null || profile.getSavingsBalance() < 0.0) {
            throw new BadRequestException("savingsBalance must be >= 0");
        }
        Optional<FinancialProfile> existing = fpRepo.findByUserId(user.getId());
        if (existing.isPresent()) {
            // Update existing rather than create duplicate
            FinancialProfile e = existing.get();
            e.setMonthlyIncome(profile.getMonthlyIncome());
            e.setMonthlyExpenses(profile.getMonthlyExpenses());
            e.setExistingLoanEmi(profile.getExistingLoanEmi());
            e.setCreditScore(profile.getCreditScore());
            e.setSavingsBalance(profile.getSavingsBalance());
            e.setUser(user);
            return fpRepo.save(e);
        }
        profile.setUser(user);
        return fpRepo.save(profile);
    }

    @Override
    public FinancialProfile getByUserId(Long userId) {
        Objects.requireNonNull(fpRepo, "FinancialProfileRepository required");
        return fpRepo.findByUserId(userId).orElse(null);
    }
}
