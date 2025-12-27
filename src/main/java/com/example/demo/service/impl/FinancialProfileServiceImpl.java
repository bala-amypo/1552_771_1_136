package com.example.demo.service.impl;

import com.example.demo.entity.FinancialProfile;
import com.example.demo.entity.User;
import com.example.demo.repository.FinancialProfileRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.FinancialProfileService;

import org.springframework.stereotype.Service;

@Service
public class FinancialProfileServiceImpl implements FinancialProfileService {

    private final FinancialProfileRepository financialProfileRepository;
    private final UserRepository userRepository;

    public FinancialProfileServiceImpl(
            FinancialProfileRepository financialProfileRepository,
            UserRepository userRepository) {
        this.financialProfileRepository = financialProfileRepository;
        this.userRepository = userRepository;
    }

    @Override
    public FinancialProfile createOrUpdate(FinancialProfile profile) {

        User user = userRepository.findById(profile.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        FinancialProfile existing =
                financialProfileRepository.findByUserId(user.getId()).orElse(null);

        if (existing != null) {
            existing.setMonthlyIncome(profile.getMonthlyIncome());
            existing.setMonthlyExpenses(profile.getMonthlyExpenses());
            existing.setExistingLoanEmi(profile.getExistingLoanEmi());
            existing.setCreditScore(profile.getCreditScore());
            existing.setSavingsBalance(profile.getSavingsBalance());

            // ⭐ FIX FOR TEST FAILURE
            existing.touch();
            return financialProfileRepository.save(existing);
        }

        profile.setUser(user);

        // ⭐ FIX FOR TEST FAILURE
        profile.touch();
        return financialProfileRepository.save(profile);
    }

    @Override
    public FinancialProfile getByUserId(Long userId) {
        return financialProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Financial profile not found"));
    }
}
