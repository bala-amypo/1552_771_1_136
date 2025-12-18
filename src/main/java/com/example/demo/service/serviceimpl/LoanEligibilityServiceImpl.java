package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.EligibilityResultRepository;
import com.example.demo.repository.FinancialProfileRepository;
import com.example.demo.repository.LoanRequestRepository;
import com.example.demo.repository.RiskAssessmentLogRepository;
import com.example.demo.service.LoanEligibilityService;

import java.sql.Timestamp;

public class LoanEligibilityServiceImpl implements LoanEligibilityService {

    private final LoanRequestRepository loanRequestRepository;
    private final FinancialProfileRepository profileRepository;
    private final EligibilityResultRepository resultRepository;
    private final RiskAssessmentLogRepository logRepository;

    // ✅ Constructor injection
    public LoanEligibilityServiceImpl(
            LoanRequestRepository loanRequestRepository,
            FinancialProfileRepository profileRepository,
            EligibilityResultRepository resultRepository,
            RiskAssessmentLogRepository logRepository
    ) {
        this.loanRequestRepository = loanRequestRepository;
        this.profileRepository = profileRepository;
        this.resultRepository = resultRepository;
        this.logRepository = logRepository;
    }

    @Override
    public EligibilityResult evaluateEligibility(Long loanRequestId) {
        LoanRequest request = loanRequestRepository.findById(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found"));

        FinancialProfile profile = profileRepository.findByUserId(request.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Financial profile not found"));

        // Calculate DTI (Debt-to-Income)
        double dti = profile.getExistingLoanEmi() / profile.getMonthlyIncome();

        // Check credit score
        boolean creditEligible = profile.getCreditScore() >= 600;

        boolean eligible = creditEligible && dti < 0.4 && request.getRequestedAmount() <= profile.getSavingsBalance() * 5;

        // Determine max eligible amount
        double maxEligibleAmount = eligible ? request.getRequestedAmount() : profile.getSavingsBalance() * 5;

        // Estimate EMI (simple fixed interest example)
        double estimatedEmi = maxEligibleAmount / request.getTenureMonths();

        // Determine risk level
        String riskLevel;
        if (dti < 0.2 && profile.getCreditScore() > 750) riskLevel = "LOW";
        else if (dti < 0.35) riskLevel = "MEDIUM";
        else riskLevel = "HIGH";

        // Save EligibilityResult
        EligibilityResult result = new EligibilityResult();
        result.setLoanRequest(request);
        result.setIsEligible(eligible);
        result.setMaxEligibleAmount(maxEligibleAmount);
        result.setEstimatedEmi(estimatedEmi);
        result.setRiskLevel(riskLevel);
        result.setRejectionReason(eligible ? null : "Credit or DTI criteria not met");
        result.setCalculatedAt(new Timestamp(System.currentTimeMillis())); // ✅ Timestamp

        resultRepository.save(result);

        // Save RiskAssessmentLog
        RiskAssessmentLog log = new RiskAssessmentLog();
        log.setLoanRequestId(request.getId());
        log.setDtiRatio(dti);
        log.setCreditCheckStatus(creditEligible ? "PASS" : "FAIL");
        log.setTimestamp(new Timestamp(System.currentTimeMillis())); // ✅ Timestamp

        logRepository.save(log);

        return result;
    }

    @Override
    public EligibilityResult getResultByRequest(Long loanRequestId) {
        return resultRepository.findByLoanRequestId(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Eligibility result not found"));
    }
}
