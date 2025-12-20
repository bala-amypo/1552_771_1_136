package com.example.demo.service.impl;

import com.example.demo.entity.EligibilityResult;
import com.example.demo.entity.FinancialProfile;
import com.example.demo.entity.LoanRequest;
import com.example.demo.repository.EligibilityResultRepository;
import com.example.demo.repository.FinancialProfileRepository;
import com.example.demo.repository.LoanRequestRepository;
import com.example.demo.service.LoanEligibilityService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
        LoanRequest loanRequest = loanRequestRepository.findById(loanRequestId)
                .orElseThrow(() -> new RuntimeException("Loan request not found"));

        // Check if already evaluated
        if(resultRepository.findByLoanRequestId(loanRequestId).isPresent()) {
            throw new RuntimeException("Eligibility already evaluated");
        }

        FinancialProfile profile = profileRepository.findByUserId(loanRequest.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Financial profile not found"));

        double dti = (profile.getMonthlyExpenses() + 
                     (profile.getExistingLoanEmi() != null ? profile.getExistingLoanEmi() : 0)) / profile.getMonthlyIncome();

        boolean eligible = dti < 0.5 && profile.getCreditScore() >= 600;

        double maxEligibleAmount = eligible ? loanRequest.getRequestedAmount() : loanRequest.getRequestedAmount() * 0.5;
        double estimatedEmi = maxEligibleAmount / loanRequest.getTenureMonths();
        String riskLevel = dti < 0.3 ? "LOW" : dti < 0.4 ? "MEDIUM" : "HIGH";
        String rejectionReason = eligible ? null : "High DTI or low credit score";

        EligibilityResult result = new EligibilityResult();
        result.setLoanRequest(loanRequest);
        result.setIsEligible(eligible);
        result.setMaxEligibleAmount(maxEligibleAmount);
        result.setEstimatedEmi(estimatedEmi);
        result.setRiskLevel(riskLevel);
        result.setRejectionReason(rejectionReason);
        result.setCalculatedAt(LocalDateTime.now());

        return resultRepository.save(result);
    }

    @Override
    public EligibilityResult getByLoanRequestId(Long loanRequestId) {
        return resultRepository.findByLoanRequestId(loanRequestId)
                .orElseThrow(() -> new RuntimeException("Eligibility result not found"));
    }
}
