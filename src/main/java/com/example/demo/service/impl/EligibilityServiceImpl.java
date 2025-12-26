package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.BadRequestException;
import com.example.demo.repository.*;

public class EligibilityServiceImpl {

    private final LoanRequestRepository loanRepo;
    private final FinancialProfileRepository profileRepo;
    private final EligibilityResultRepository resultRepo;

    public EligibilityServiceImpl(
            LoanRequestRepository loanRepo,
            FinancialProfileRepository profileRepo,
            EligibilityResultRepository resultRepo) {
        this.loanRepo = loanRepo;
        this.profileRepo = profileRepo;
        this.resultRepo = resultRepo;
    }

    public EligibilityResult evaluateEligibility(Long loanRequestId) {
        if (resultRepo.findByLoanRequestId(loanRequestId).isPresent())
            throw new BadRequestException("Eligibility already evaluated");

        LoanRequest lr = loanRepo.findById(loanRequestId).orElseThrow();
        FinancialProfile fp = profileRepo.findByUserId(lr.getUser().getId()).orElseThrow();

        EligibilityResult er = new EligibilityResult();
        er.setMaxEligibleAmount(Math.max(0, fp.getMonthlyIncome() * 10));
        return resultRepo.save(er);
    }

    public EligibilityResult getByLoanRequestId(Long id) {
        return resultRepo.findByLoanRequestId(id).orElse(null);
    }
}
