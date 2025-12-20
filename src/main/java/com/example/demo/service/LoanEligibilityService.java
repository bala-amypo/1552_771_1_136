package com.example.demo.service;

import com.example.demo.entity.EligibilityResult;

public interface LoanEligibilityService {

    /**
     * Evaluate loan eligibility
     * @param loanRequestId loan request id
     * @return EligibilityResult
     */
    EligibilityResult evaluateEligibility(Long loanRequestId);

    /**
     * Get eligibility result by loan request ID
     * @param loanRequestId loan request id
     * @return EligibilityResult
     */
    EligibilityResult getByLoanRequestId(Long loanRequestId);
}
