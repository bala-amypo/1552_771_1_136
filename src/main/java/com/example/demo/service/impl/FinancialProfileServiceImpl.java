package com.example.demo.service.impl;

import com.example.demo.entity.FinancialProfile;
import com.example.demo.repository.FinancialProfileRepository;
import com.example.demo.service.FinancialProfileService;
import com.example.demo.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service 
public class FinancialProfileServiceImpl implements FinancialProfileService {

    @Autowired
    private FinancialProfileRepository financialProfileRepository;

    @Override
    public FinancialProfile createOrUpdate(FinancialProfile profile) {
        // Validation for Test 16
        if (profile.getCreditScore() != null && 
           (profile.getCreditScore() < 300 || profile.getCreditScore() > 850)) {
            throw new BadRequestException("Invalid credit score: " + profile.getCreditScore());
        }
        return financialProfileRepository.save(profile);
    }

    @Override
    public Optional<FinancialProfile> getByUserId(Long userId) {
        return financialProfileRepository.findByUserId(userId);
    }
}