package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.BadRequestException;
import com.example.demo.repository.*;

public class RiskAssessmentServiceImpl {

    private final LoanRequestRepository loanRepo;
    private final FinancialProfileRepository fpRepo;
    private final RiskAssessmentRepository raRepo;

    public RiskAssessmentServiceImpl(LoanRequestRepository l, FinancialProfileRepository f, RiskAssessmentRepository r) {
        this.loanRepo = l;
        this.fpRepo = f;
        this.raRepo = r;
    }

    public RiskAssessment assessRisk(Long loanId) {
        if (raRepo.findByLoanRequestId(loanId).isPresent())
            throw new BadRequestException("Already assessed");

        LoanRequest lr = loanRepo.findById(loanId).orElseThrow();
        FinancialProfile fp = fpRepo.findByUserId(lr.getUser().getId()).orElseThrow();

        RiskAssessment ra = new RiskAssessment();
        ra.setLoanRequest(lr);

        double emi = fp.getExistingLoanEmi() == null ? 0 : fp.getExistingLoanEmi();
        double income = fp.getMonthlyIncome() == 0 ? 1 : fp.getMonthlyIncome();
        ra.setDtiRatio(emi / income);
        ra.setRiskScore(Math.min(100, ra.getDtiRatio() * 100));

        return raRepo.save(ra);
    }

    public RiskAssessment getByLoanRequestId(Long id) {
        return raRepo.findByLoanRequestId(id).orElse(null);
    }
}
