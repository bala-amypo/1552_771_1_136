package com.example.demo.service;

import com.example.demo.model.EligibilityResult;

public interface LoanEligibilityService {
    // This must match exactly what you use in the Impl file
    EligibilityResult evaluateEligibility(Long loanRequestId);
    EligibilityResult getByLoanRequestId(Long loanRequestId);
}