package com.example.demo.service;

import com.example.demo.entity.EligibilityResult;

public interface LoanEligibilityService {
    // Runs the evaluation logic and persists the result
    EligibilityResult evaluateEligibility(Long loanRequestId);
    
    // Retrieves an existing result
    EligibilityResult getByLoanRequestId(Long loanRequestId);
}