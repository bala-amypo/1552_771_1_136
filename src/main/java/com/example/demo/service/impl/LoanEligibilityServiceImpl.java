package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.service.LoanEligibilityService;
import org.springframework.stereotype.Service;

@Service
public class LoanEligibilityServiceImpl implements LoanEligibilityService {

    private final LoanRequestRepository loanRequestRepository;
    private final FinancialProfileRepository profileRepository;
    private final EligibilityResultRepository resultRepository;

    // Constructor injection only
    public LoanEligibilityServiceImpl(LoanRequestRepository loanRequestRepository, 
                                     FinancialProfileRepository profileRepository, 
                                     EligibilityResultRepository resultRepository) {
        this.loanRequestRepository = loanRequestRepository;
        this.profileRepository = profileRepository;
        this.resultRepository = resultRepository;
    }

    @Override
    public EligibilityResult evaluateEligibility(Long loanRequestId) {
        // 1. Prevent duplicate evaluation
        if (resultRepository.findByLoanRequestId(loanRequestId).isPresent()) {
            throw new BadRequestException("Eligibility already evaluated");
        }

        // 2. Fetch Loan Request
        LoanRequest request = loanRequestRepository.findById(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found"));

        // 3. Fetch Financial Profile
        FinancialProfile profile = profileRepository.findByUserId(request.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Financial profile not found"));

        EligibilityResult result = new EligibilityResult();
        result.setLoanRequest(request);

        // 4. Calculate DTI (Debt-to-Income ratio)
        double monthlyIncome = profile.getMonthlyIncome();
        double emiObligations = (profile.getExistingLoanEmi() != null) ? profile.getExistingLoanEmi() : 0.0;
        double totalMonthlyObligations = profile.getMonthlyExpenses() + emiObligations;
        double dtiRatio = totalMonthlyObligations / monthlyIncome;

        // 5. Apply Business Rules
        // Rule: Credit Score must be within 300-900 range
        if (profile.getCreditScore() < 600) {
            result.setIsEligible(false);
            result.setRejectionReason("Low creditScore");
            result.setRiskLevel("HIGH");
        } 
        // Rule: DTI threshold
        else if (dtiRatio > 0.5) {
            result.setIsEligible(false);
            result.setRejectionReason("High DTI ratio");
            result.setRiskLevel("HIGH");
        } 
        else {
            result.setIsEligible(true);
            // Logic: Max amount based on income
            double maxAmount = monthlyIncome * 10; 
            result.setMaxEligibleAmount(maxAmount);
            result.setEstimatedEmi(maxAmount / request.getTenureMonths());
            
            // Determine riskLevel
            if (profile.getCreditScore() > 750) {
                result.setRiskLevel("LOW");
            } else {
                result.setRiskLevel("MEDIUM");
            }
        }

        return resultRepository.save(result);
    }

    @Override
    public EligibilityResult getByLoanRequestId(Long loanRequestId) {
        return resultRepository.findByLoanRequestId(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Eligibility result not found"));
    }
}