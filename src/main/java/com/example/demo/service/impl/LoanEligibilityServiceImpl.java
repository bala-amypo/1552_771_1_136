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
    @Transactional
    public EligibilityResult evaluateEligibility(Long loanRequestId) {
        // 1. Check if already evaluated
        if (eligibilityResultRepository.findByLoanRequestId(loanRequestId).isPresent()) {
            throw new BadRequestException("Eligibility already evaluated");
        }

        // 2. Fetch dependencies
        LoanRequest request = loanRequestRepository.findById(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found"));
        FinancialProfile profile = profileRepository.findByUserId(request.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Financial profile not found for user"));

        EligibilityResult result = new EligibilityResult();
        result.setLoanRequest(request);

        // 3. Business Rules Logic
        double monthlyIncome = profile.getMonthlyIncome();
        double currentExpenses = profile.getMonthlyExpenses() + profile.getExistingLoanEmi();
        double dti = (currentExpenses / monthlyIncome) * 100;
        int creditScore = profile.getCreditScore();

        // 4. Determine Risk Level
        if (creditScore >= 750) result.setRiskLevel("LOW");
        else if (creditScore >= 650) result.setRiskLevel("MEDIUM");
        else result.setRiskLevel("HIGH");

        // 5. Calculate Max Amount & Eligibility
        double maxAllowedDti = 50.0; // Rule: Expenses shouldn't exceed 50% of income
        if (dti > maxAllowedDti || creditScore < 600) {
            result.setIsEligible(false);
            result.setRejectionReason(dti > maxAllowedDti ? "DTI too high" : "Credit score too low");
            result.setMaxEligibleAmount(0.0);
            result.setEstimatedEmi(0.0);
            request.setStatus("REJECTED");
        } else {
            result.setIsEligible(true);
            // Example Max Amount calculation: Income * 10
            result.setMaxEligibleAmount(monthlyIncome * 10);
            double emi = (result.getMaxEligibleAmount() / request.getTenureMonths()) * 1.1; // Amount/tenure + 10% interest
            result.setEstimatedEmi(emi);
            request.setStatus("APPROVED");
        }

        loanRequestRepository.save(request);
        return eligibilityResultRepository.save(result);
    }

    @Override
    public EligibilityResult getByLoanRequestId(Long loanRequestId) {
        return eligibilityResultRepository.findByLoanRequestId(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Result not found"));
    }
}