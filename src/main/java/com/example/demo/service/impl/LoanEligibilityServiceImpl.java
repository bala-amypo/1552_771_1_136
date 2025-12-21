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

    public LoanEligibilityServiceImpl(LoanRequestRepository loanRequestRepository, 
                                     FinancialProfileRepository profileRepository, 
                                     EligibilityResultRepository resultRepository) {
        this.loanRequestRepository = loanRequestRepository;
        this.profileRepository = profileRepository;
        this.resultRepository = resultRepository;
    }

    @Override
    public EligibilityResult evaluateEligibility(Long loanRequestId) {
        if (resultRepository.findByLoanRequestId(loanRequestId).isPresent()) {
            throw new BadRequestException("Eligibility already evaluated");
        }

        LoanRequest request = loanRequestRepository.findById(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found"));

        FinancialProfile profile = profileRepository.findByUserId(request.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Financial profile not found"));

        EligibilityResult result = new EligibilityResult();
        result.setLoanRequest(request);

        double monthlyIncome = profile.getMonthlyIncome();
        double expenses = profile.getMonthlyExpenses() + (profile.getExistingLoanEmi() != null ? profile.getExistingLoanEmi() : 0);
        double dti = expenses / monthlyIncome;

        if (profile.getCreditScore() < 600) {
            result.setIsEligible(false);
            result.setRejectionReason("Low creditScore");
            result.setRiskLevel("HIGH");
        } else if (dti > 0.5) {
            result.setIsEligible(false);
            result.setRejectionReason("High DTI ratio");
            result.setRiskLevel("HIGH");
        } else {
            result.setIsEligible(true);
            double maxAmount = monthlyIncome * 10;
            result.setMaxEligibleAmount(maxAmount);
            result.setEstimatedEmi(maxAmount / request.getTenureMonths());
            result.setRiskLevel(profile.getCreditScore() > 750 ? "LOW" : "MEDIUM");
        }

        return resultRepository.save(result);
    }

    // This was the missing method causing the error
    @Override
    public EligibilityResult getByLoanRequestId(Long loanRequestId) {
        return resultRepository.findByLoanRequestId(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Eligibility result not found"));
    }
}