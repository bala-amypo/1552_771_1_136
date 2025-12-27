package com.example.demo.service;

import com.example.demo.entity.FinancialProfile;
import com.example.demo.repository.FinancialProfileRepository;
import com.example.demo.exception.BadRequestException; // Ensure this import matches your project
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FinancialProfileService {

    @Autowired
    private FinancialProfileRepository financialProfileRepository;

    public FinancialProfile createOrUpdate(FinancialProfile profile) {
        // Fix for Test 16: Validate credit score range (standard is 300-850)
        if (profile.getCreditScore() != null && 
           (profile.getCreditScore() < 300 || profile.getCreditScore() > 850)) {
            throw new BadRequestException("Invalid credit score: " + profile.getCreditScore());
        }

        return financialProfileRepository.save(profile);
    }
}