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

    @Override
    public EligibilityResult evaluate(Long loanRequestId) {

        LoanRequest loan = loanRepo.findById(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found"));

        FinancialProfile profile = profileRepo.findByUserId(loan.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Financial profile not found"));

        double obligations = profile.getMonthlyExpenses() +
                (profile.getExistingLoanEmi() == null ? 0 : profile.getExistingLoanEmi());

        double dti = obligations / profile.getMonthlyIncome();

        EligibilityResult result = new EligibilityResult();
        result.setLoanRequest(loan);

        if (profile.getCreditScore() < 600 || dti > 0.6) {
            result.setIsEligible(false);
            result.setRiskLevel("HIGH");
            result.setRejectionReason("High risk");
        } else {
            result.setIsEligible(true);
            result.setRiskLevel(dti < 0.3 ? "LOW" : "MEDIUM");
            result.setMaxEligibleAmount(profile.getMonthlyIncome() * 10);
            result.setEstimatedEmi(result.getMaxEligibleAmount() / loan.getTenureMonths());
        }

        RiskAssessmentLog log = new RiskAssessmentLog();
        log.setLoanRequestId(loanRequestId);
        log.setDtiRatio(dti);
        log.setCreditCheckStatus(result.getIsEligible() ? "APPROVED" : "REJECTED");
        logRepo.save(log);

        return resultRepo.save(result);
    }
}
