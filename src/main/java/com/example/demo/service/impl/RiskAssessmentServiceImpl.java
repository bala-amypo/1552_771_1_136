package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.BadRequestException;
import com.example.demo.repository.*;

public class RiskAssessmentServiceImpl {

    private final LoanRequestRepository loanRepo;
    private final FinancialProfileRepository profileRepo;
    private final RiskAssessmentRepository riskRepo;

    public RiskAssessmentServiceImpl(
            LoanRequestRepository loanRepo,
            FinancialProfileRepository profileRepo,
            RiskAssessmentRepository riskRepo) {
        this.loanRepo = loanRepo;
        this.profileRepo = profileRepo;
        this.riskRepo = riskRepo;
    }

    public RiskAssessment assessRisk(Long loanRequestId) {
        if (riskRepo.findByLoanRequestId(loanRequestId).isPresent())
            throw new BadRequestException("Risk already assessed");

        FinancialProfile fp = profileRepo.findByUserId(
                loanRepo.findById(loanRequestId).orElseThrow().getUser().getId()
        ).orElseThrow();

        RiskAssessment ra = new RiskAssessment();
        double income = fp.getMonthlyIncome() == null ? 0 : fp.getMonthlyIncome();
        ra.setDtiRatio(income == 0 ? 0 : fp.getMonthlyExpenses() / income);
        ra.setRiskScore(50.0);
        return riskRepo.save(ra);
    }

    public RiskAssessment getByLoanRequestId(Long id) {
        return riskRepo.findByLoanRequestId(id).orElse(null);
    }
}
