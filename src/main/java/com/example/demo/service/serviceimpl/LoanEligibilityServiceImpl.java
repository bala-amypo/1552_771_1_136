package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.*;
import com.example.demo.repository.*;
import com.example.demo.service.LoanEligibilityService;
import org.springframework.stereotype.Service;

@Service
public class LoanEligibilityServiceImpl implements LoanEligibilityService {

    private final LoanRequestRepository loanRepo;
    private final FinancialProfileRepository profileRepo;
    private final EligibilityResultRepository resultRepo;
    private final RiskAssessmentLogRepository logRepo;

    public LoanEligibilityServiceImpl(
            LoanRequestRepository loanRepo,
            FinancialProfileRepository profileRepo,
            EligibilityResultRepository resultRepo,
            RiskAssessmentLogRepository logRepo) {
        this.loanRepo = loanRepo;
        this.profileRepo = profileRepo;
        this.resultRepo = resultRepo;
        this.logRepo = logRepo;
    }

    public EligibilityResult evaluateEligibility(Long loanRequestId) {
        LoanRequest loan = loanRepo.findById(loanRequestId).orElseThrow(() -> new ResourceNotFoundException("Loan not found"));
        FinancialProfile profile = profileRepo.findByUserId(loan.getUser().getId()).orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        double dti = profile.getExistingLoanEmi() / profile.getMonthlyIncome();
        String risk = dti < 0.3 ? "LOW" : dti < 0.6 ? "MEDIUM" : "HIGH";

        EligibilityResult result = new EligibilityResult();
        result.setLoanRequest(loan);
        result.setIsEligible(dti < 0.6 && profile.getCreditScore() >= 650);
        result.setRiskLevel(risk);
        result.setEstimatedEmi(loan.getRequestedAmount() / loan.getTenureMonths());
        result.setMaxEligibleAmount(profile.getMonthlyIncome() * 10);

        RiskAssessmentLog log = new RiskAssessmentLog();
        log.setLoanRequestId(loanRequestId);
        log.setDtiRatio(dti);
        log.setCreditCheckStatus("CHECKED");

        logRepo.save(log);
        return resultRepo.save(result);
    }

    public EligibilityResult getResultByRequest(Long requestId) {
        return resultRepo.findByLoanRequestId(requestId).orElseThrow(() -> new ResourceNotFoundException("Result not found"));
    }
}
