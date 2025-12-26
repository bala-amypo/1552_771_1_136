package com.example.demo.service;

import com.example.demo.entity.EligibilityResult;

public interface LoanEligibilityService {
    /**
     * Evaluates loan eligibility using DTI, credit score, and income rules.
     */
    EligibilityResult evaluateEligibility(Long loanRequestId);

    /**
     * Retrieves the saved eligibility result for a specific loan request.
     */
    EligibilityResult getByLoanRequestId(Long loanRequestId);
}