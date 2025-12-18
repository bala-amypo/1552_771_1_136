package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.LoanEligibilityService;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;

public class LoanEligibilityServiceImpl implements LoanEligibilityService {

    private final LoanRequestRepository loanRepository;
    private final FinancialProfileRepository profileRepository;
    private final EligibilityResultRepository resultRepository;

    public LoanEligibilityServiceImpl(LoanRequestRepository loanRepository,
                                      FinancialProfileRepository profileRepository,
                                      EligibilityResultRepository resultRepository) {
        this.loanRepository = loanRepository;
        this.profileRepository = profileRepository;
        this.resultRepository = resultRepository;
    }

    @Override
    public EligibilityResult evaluateEligibility(Long loanRequestId) {

        if (resultRepository.findByLoanRequestId(loanRequestId).isPresent()) {
            throw new BadRequestException("Eligibility already evaluated");
        }

        LoanRequest loan = loanRepository.findById(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found"));

        FinancialProfile profile = profileRepository.findByUserId(loan.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Financial profile not found"));

        double obligations = profile.getMonthlyExpenses() +
                (profile.getExistingLoanEmi() == null ? 0 : profile.getExistingLoanEmi());

        double dti = obligations / profile.getMonthlyIncome();

        EligibilityResult result = new EligibilityResult();
        result.setLoanRequest(loan);

        if (profile.getCreditScore() < 600 || dti > 0.6) {
            result.setIsEligible(false);
            result.setRiskLevel("HIGH");
            result.setRejectionReason("High risk based on credit or income");
            result.setMaxEligibleAmount(0.0);
            result.setEstimatedEmi(0.0);
        } else {
            double maxAmount = profile.getMonthlyIncome() * 20;
            double emi = maxAmount / loan.getTenureMonths();

            result.setIsEligible(true);
            result.setRiskLevel(dti < 0.3 ? "LOW" : "MEDIUM");
            result.setMaxEligibleAmount(maxAmount);
            result.setEstimatedEmi(emi);
        }

        return resultRepository.save(result);
    }

    @Override
    public EligibilityResult getByLoanRequestId(Long loanRequestId) {
        return resultRepository.findByLoanRequestId(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Eligibility result not found"));
    }
}
