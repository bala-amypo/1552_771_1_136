package com.example.demo.service;

import com.example.demo.entity.EligibilityResult;

public interface LoanEligibilityService {
    // Evaluates eligibility based on DTI, income, and credit score
    EligibilityResult evaluateEligibility(Long loanRequestId);
    
    // Retrieves an existing result
    EligibilityResult getByLoanRequestId(Long loanRequestId);
}