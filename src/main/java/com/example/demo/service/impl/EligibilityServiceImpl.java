package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.BadRequestException;
import com.example.demo.repository.*;

public class EligibilityServiceImpl {

    private final LoanRequestRepository loanRepo;
    private final FinancialProfileRepository fpRepo;
    private final EligibilityResultRepository erRepo;

    public EligibilityServiceImpl(LoanRequestRepository l, FinancialProfileRepository f, EligibilityResultRepository e) {
        this.loanRepo = l;
        this.fpRepo = f;
        this.erRepo = e;
    }

    public EligibilityResult evaluateEligibility(Long loanId) {
        if (erRepo.findByLoanRequestId(loanId).isPresent())
            throw new BadRequestException("Already evaluated");

        LoanRequest lr = loanRepo.findById(loanId).orElseThrow();
        FinancialProfile fp = fpRepo.findByUserId(lr.getUser().getId()).orElseThrow();

        EligibilityResult er = new EligibilityResult();
        er.setLoanRequest(lr);
        er.setEligible(true);
        er.setMaxEligibleAmount(fp.getMonthlyIncome() * 10);

        return erRepo.save(er);
    }

    public EligibilityResult getByLoanRequestId(Long id) {
        return erRepo.findByLoanRequestId(id).orElse(null);
    }
}
