// src/main/java/com/example/demo/service/impl/RiskAssessmentServiceImpl.java
package com.example.demo.service.impl;

import com.example.demo.entity.FinancialProfile;
import com.example.demo.entity.LoanRequest;
import com.example.demo.entity.RiskAssessment;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.FinancialProfileRepository;
import com.example.demo.repository.LoanRequestRepository;
import com.example.demo.repository.RiskAssessmentRepository;
import com.example.demo.service.RiskAssessmentService;

import java.util.Objects;

public class RiskAssessmentServiceImpl implements RiskAssessmentService {
    private final LoanRequestRepository lrRepo;
    private final FinancialProfileRepository fpRepo;
    private final RiskAssessmentRepository raRepo;

    public RiskAssessmentServiceImpl(LoanRequestRepository lrRepo,
                                     FinancialProfileRepository fpRepo,
                                     RiskAssessmentRepository raRepo) {
        this.lrRepo = lrRepo; this.fpRepo = fpRepo; this.raRepo = raRepo;
    }

    @Override
    public RiskAssessment assessRisk(Long loanRequestId) {
        Objects.requireNonNull(lrRepo); Objects.requireNonNull(fpRepo); Objects.requireNonNull(raRepo);

        if (raRepo.findByLoanRequestId(loanRequestId).isPresent())
            throw new BadRequestException("Risk already assessed");

        LoanRequest lr = lrRepo.findById(loanRequestId)
            .orElseThrow(() -> new ResourceNotFoundException("Loan request not found"));
        FinancialProfile fp = fpRepo.findByUserId(lr.getUser().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Financial profile not found"));

        double income = fp.getMonthlyIncome();
        double obligations = fp.getMonthlyExpenses() + fp.getExistingLoanEmi();
        double dti = income <= 0 ? 0.0 : obligations / income;

        String creditCheckStatus = fp.getCreditScore() >= 700 ? "APPROVED"
                : (fp.getCreditScore() >= 600 ? "PENDING_REVIEW" : "REJECTED");

        // Simple risk score 0..100 combining dti and credit score
        double riskScore = Math.max(0, Math.min(100,
                (dti * 100 * 0.6) + ((900 - fp.getCreditScore()) * (100.0 / 600.0) * 0.4)));

        RiskAssessment ra = new RiskAssessment();
        ra.setLoanRequestId(loanRequestId);
        ra.setDtiRatio(dti);
        ra.setCreditCheckStatus(creditCheckStatus);
        ra.setRiskScore(riskScore);
        return raRepo.save(ra);
    }

    @Override
    public RiskAssessment getByLoanRequestId(Long loanRequestId) {
        Objects.requireNonNull(raRepo);
        return raRepo.findByLoanRequestId(loanRequestId).orElse(null);
    }
}
