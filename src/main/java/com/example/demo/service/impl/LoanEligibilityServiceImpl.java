package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.LoanEligibilityService;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoanEligibilityServiceImpl implements LoanEligibilityService {

    private final LoanRequestRepository loanRequestRepository;
    private final FinancialProfileRepository profileRepository;
    private final EligibilityResultRepository eligibilityRepository;

    public LoanEligibilityServiceImpl(LoanRequestRepository loanRequestRepository, 
                                     FinancialProfileRepository profileRepository, 
                                     EligibilityResultRepository eligibilityRepository) {
        this.loanRequestRepository = loanRequestRepository;
        this.profileRepository = profileRepository;
        this.eligibilityRepository = eligibilityRepository;
    }

    @Override
    @Transactional // Required to save data to the database
    public EligibilityResult evaluateEligibility(Long loanRequestId) {
        // Fetch data
        LoanRequest request = loanRequestRepository.findById(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));
        
        FinancialProfile profile = profileRepository.findByUserId(request.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        // Calculation Logic
        EligibilityResult result = new EligibilityResult();
        result.setLoanRequest(request);
        
        double dti = (profile.getMonthlyExpenses() / profile.getMonthlyIncome()) * 100;
        
        if (dti > 50 || profile.getCreditScore() < 600) {
            result.setIsEligible(false);
            result.setRejectionReason("DTI too high or Credit Score too low");
            result.setMaxEligibleAmount(0.0);
            result.setEstimatedEmi(0.0);
            result.setRiskLevel("HIGH");
        } else {
            result.setIsEligible(true);
            result.setMaxEligibleAmount(profile.getMonthlyIncome() * 10);
            result.setEstimatedEmi(result.getMaxEligibleAmount() / request.getTenureMonths());
            result.setRiskLevel(profile.getCreditScore() > 750 ? "LOW" : "MEDIUM");
        }

        // Save and return
        return eligibilityRepository.save(result); // This triggers the SQL INSERT
    }

    @Override
    public EligibilityResult getByLoanRequestId(Long loanRequestId) {
        return eligibilityRepository.findByLoanRequestId(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Result not found"));
    }
}