package com.example.demo.service;

import com.example.demo.entity.EligibilityResult;

public interface LoanEligibilityService {
    // Evaluates eligibility based on business rules and persists the result
    EligibilityResult evaluateEligibility(Long loanRequestId);
    
    // Retrieves eligibility result for a specific loan request
    EligibilityResult getByLoanRequestId(Long loanRequestId);
}