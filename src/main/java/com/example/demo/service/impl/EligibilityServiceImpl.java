// src/main/java/com/example/demo/service/impl/EligibilityServiceImpl.java
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

import java.util.Objects;

public class EligibilityServiceImpl implements LoanEligibilityService {
    private final LoanRequestRepository lrRepo;
    private final FinancialProfileRepository fpRepo;
    private final EligibilityResultRepository erRepo;

    public EligibilityServiceImpl(LoanRequestRepository lrRepo,
                                  FinancialProfileRepository fpRepo,
                                  EligibilityResultRepository erRepo) {
        this.lrRepo = lrRepo; this.fpRepo = fpRepo; this.erRepo = erRepo;
    }

    @Override
    public EligibilityResult evaluateEligibility(Long loanRequestId) {
        Objects.requireNonNull(lrRepo); Objects.requireNonNull(fpRepo); Objects.requireNonNull(erRepo);
        // Duplicate check
        if (erRepo.findByLoanRequestId(loanRequestId).isPresent())
            throw new BadRequestException("Eligibility already evaluated");

        LoanRequest lr = lrRepo.findById(loanRequestId)
            .orElseThrow(() -> new ResourceNotFoundException("Loan request not found"));
        FinancialProfile fp = fpRepo.findByUserId(lr.getUser().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Financial profile not found"));

        double income = fp.getMonthlyIncome();
        double obligations = fp.getMonthlyExpenses() + fp.getExistingLoanEmi();
        double dti = income <= 0 ? 0.0 : obligations / income;

        int score = fp.getCreditScore();
        if (score < 300 || score > 900) throw new BadRequestException("creditScore out of range");

        // Simple business rule: maxEligibleAmount proportional to disposable income and credit score factor
        double disposable = Math.max(0, income - obligations);
        double creditFactor = (score - 300) / 600.0; // maps 300..900 to 0..1
        double maxEligible = Math.max(0.0, disposable * 20.0 * (0.5 + 0.5 * creditFactor)); // arbitrary scaling

        // Estimated EMI = principal/tenure + interest (simple)
        int tenure = lr.getTenureMonths();
        double baseEmi = tenure > 0 ? maxEligible / tenure : 0.0;
        double interest = baseEmi * 0.01 * (1 + dti); // add small interest by dti
        double estimatedEmi = baseEmi + interest;

        String riskLevel = dti < 0.25 ? "LOW" : (dti < 0.45 ? "MEDIUM" : "HIGH");
        boolean isEligible = maxEligible >= lr.getRequestedAmount() && !"HIGH".equals(riskLevel);
        String rejectionReason = isEligible ? null : "Insufficient eligibility or high risk";

        EligibilityResult er = new EligibilityResult();
        er.setLoanRequest(lr);
        er.setIsEligible(isEligible);
        er.setMaxEligibleAmount(maxEligible);
        er.setEstimatedEmi(estimatedEmi);
        er.setRiskLevel(riskLevel);
        er.setRejectionReason(rejectionReason);
        return erRepo.save(er);
    }

    @Override
    public EligibilityResult getByLoanRequestId(Long loanRequestId) {
        Objects.requireNonNull(erRepo);
        return erRepo.findByLoanRequestId(loanRequestId).orElse(null);
    }
}
