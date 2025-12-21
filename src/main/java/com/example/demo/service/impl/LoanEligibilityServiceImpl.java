package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.LoanEligibilityService;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoanEligibilityServiceImpl implements LoanEligibilityService {

    private final LoanRequestRepository loanRequestRepository;
    private final FinancialProfileRepository profileRepository;
    private final EligibilityResultRepository eligibilityResultRepository;

    public LoanEligibilityServiceImpl(LoanRequestRepository loanRequestRepository, 
                                     FinancialProfileRepository profileRepository, 
                                     EligibilityResultRepository eligibilityResultRepository) {
        this.loanRequestRepository = loanRequestRepository;
        this.profileRepository = profileRepository;
        this.eligibilityResultRepository = eligibilityResultRepository;
    }

    @Override
    @Transactional // Critical for database persistence
    public EligibilityResult evaluateEligibility(Long loanRequestId) {
        // Prevent duplicate evaluations
        if (eligibilityResultRepository.findByLoanRequestId(loanRequestId).isPresent()) {
            throw new BadRequestException("Eligibility already evaluated");
        }

        LoanRequest request = loanRequestRepository.findById(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found"));
        
        FinancialProfile profile = profileRepository.findByUserId(request.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Financial profile not found"));

        EligibilityResult result = new EligibilityResult();
        result.setLoanRequest(request);

        // Logic: DTI and Credit Score rules
        double monthlyIncome = profile.getMonthlyIncome();
        double dti = ((profile.getMonthlyExpenses() + profile.getExistingLoanEmi()) / monthlyIncome) * 100;
        
        if (profile.getCreditScore() < 600 || dti > 50) {
            result.setIsEligible(false);
            result.setRejectionReason(dti > 50 ? "DTI too high" : "Credit score too low");
            result.setMaxEligibleAmount(0.0);
            result.setEstimatedEmi(0.0);
            result.setRiskLevel("HIGH");
            request.setStatus("REJECTED");
        } else {
            result.setIsEligible(true);
            result.setMaxEligibleAmount(monthlyIncome * 12);
            result.setEstimatedEmi((result.getMaxEligibleAmount() / request.getTenureMonths()) * 1.1);
            result.setRiskLevel(profile.getCreditScore() >= 750 ? "LOW" : "MEDIUM");
            request.setStatus("APPROVED");
        }

        loanRequestRepository.save(request); // Updates loan_requests table
        return eligibilityResultRepository.save(result); // Saves to eligibility_results table
    }

    @Override
    public EligibilityResult getByLoanRequestId(Long loanRequestId) {
        return eligibilityResultRepository.findByLoanRequestId(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Eligibility result not found"));
    }
}