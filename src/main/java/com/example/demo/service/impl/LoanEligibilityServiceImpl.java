package com.example.demo.service.impl;

import com.example.demo.entity.EligibilityResult;
import com.example.demo.entity.FinancialProfile;
import com.example.demo.entity.LoanRequest;
import com.example.demo.repository.EligibilityResultRepository;
import com.example.demo.repository.FinancialProfileRepository;
import com.example.demo.repository.LoanRequestRepository;
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

    // Constructor Injection
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
        // Enforce uniqueness: throw exception if already evaluated
        if (eligibilityResultRepository.findByLoanRequestId(loanRequestId).isPresent()) {
            throw new BadRequestException("Eligibility already evaluated");
        }

        // Fetch required data
        LoanRequest request = loanRequestRepository.findById(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found"));
        
        FinancialProfile profile = profileRepository.findByUserId(request.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Financial profile not found for user"));

        EligibilityResult result = new EligibilityResult();
        result.setLoanRequest(request);

        // Business Rule: Calculate DTI (Debt-to-Income)
        double monthlyIncome = profile.getMonthlyIncome();
        double existingObligations = profile.getMonthlyExpenses() + profile.getExistingLoanEmi();
        double dti = (existingObligations / monthlyIncome) * 100;
        int creditScore = profile.getCreditScore();

        // Determine Risk Level
        if (creditScore >= 750) {
            result.setRiskLevel("LOW");
        } else if (creditScore >= 650) {
            result.setRiskLevel("MEDIUM");
        } else {
            result.setRiskLevel("HIGH");
        }

        // Apply Eligibility Rules
        if (creditScore < 600 || dti > 50.0) {
            result.setIsEligible(false);
            result.setRejectionReason(creditScore < 600 ? "Credit score below 600" : "DTI exceeds 50%");
            result.setMaxEligibleAmount(0.0);
            result.setEstimatedEmi(0.0);
            request.setStatus("REJECTED");
        } else {
            result.setIsEligible(true);
            // Calculate max amount: monthlyIncome * 10 (example rule)
            double maxAmount = monthlyIncome * 10;
            result.setMaxEligibleAmount(maxAmount);
            
            // Calculate EMI: (Amount / Tenure) + 10% interest
            double emi = (maxAmount / request.getTenureMonths()) * 1.1;
            result.setEstimatedEmi(emi);
            request.setStatus("APPROVED");
        }

        // Update loan request status and save result
        loanRequestRepository.save(request);
        return eligibilityResultRepository.save(result);
    }

    @Override
    public EligibilityResult getByLoanRequestId(Long loanRequestId) {
        return eligibilityResultRepository.findByLoanRequestId(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Eligibility result not found"));
    }
}