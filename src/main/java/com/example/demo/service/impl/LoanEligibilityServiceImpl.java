package com.example.demo.service.impl;

import com.example.demo.entity.EligibilityResult;
import com.example.demo.entity.FinancialProfile;
import com.example.demo.entity.LoanRequest;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.EligibilityResultRepository;
import com.example.demo.repository.FinancialProfileRepository;
import com.example.demo.repository.LoanRequestRepository;
import com.example.demo.service.LoanEligibilityService;

import java.time.LocalDateTime;

public class LoanEligibilityServiceImpl implements LoanEligibilityService {

    private final LoanRequestRepository loanRequestRepository;
    private final FinancialProfileRepository financialProfileRepository;
    private final EligibilityResultRepository eligibilityResultRepository;

    public LoanEligibilityServiceImpl(LoanRequestRepository loanRequestRepository,
                                      FinancialProfileRepository financialProfileRepository,
                                      EligibilityResultRepository eligibilityResultRepository) {
        this.loanRequestRepository = loanRequestRepository;
        this.financialProfileRepository = financialProfileRepository;
        this.eligibilityResultRepository = eligibilityResultRepository;
    }

    @Override
    public EligibilityResult evaluateEligibility(Long loanRequestId) {
        LoanRequest loanRequest = loanRequestRepository.findById(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found"));

        if (eligibilityResultRepository.findByLoanRequestId(loanRequestId).isPresent()) {
            throw new BadRequestException("Eligibility already evaluated");
        }

        FinancialProfile profile = financialProfileRepository.findByUserId(loanRequest.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Financial profile not found"));

        double totalObligations = profile.getMonthlyExpenses() + 
                                  (profile.getExistingLoanEmi() != null ? profile.getExistingLoanEmi() : 0);
        double dti = totalObligations / profile.getMonthlyIncome();

        boolean isEligible = profile.getCreditScore() >= 300 && profile.getCreditScore() <= 900 && dti < 0.5;

        double maxEligibleAmount = isEligible ? loanRequest.getRequestedAmount() * (1 - dti) : 0.0;
        double estimatedEmi = isEligible ? maxEligibleAmount / loanRequest.getTenureMonths() : 0.0;
        String riskLevel;
        if (!isEligible) riskLevel = "HIGH";
        else if (dti < 0.2) riskLevel = "LOW";
        else riskLevel = "MEDIUM";

        EligibilityResult result = new EligibilityResult();
        result.setLoanRequest(loanRequest);
        result.setIsEligible(isEligible);
        result.setMaxEligibleAmount(maxEligibleAmount);
        result.setEstimatedEmi(estimatedEmi);
        result.setRiskLevel(riskLevel);
        if (!isEligible) result.setRejectionReason("DTI too high or low credit score");
        result.setCalculatedAt(LocalDateTime.now());

        return eligibilityResultRepository.save(result);
    }

    @Override
    public EligibilityResult getByLoanRequestId(Long loanRequestId) {
        return eligibilityResultRepository.findByLoanRequestId(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Eligibility result not found"));
    }
}
