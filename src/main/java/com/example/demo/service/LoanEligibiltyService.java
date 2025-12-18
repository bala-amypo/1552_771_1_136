package com.example.loan.service;

import com.example.loan.entity.EligibilityResult;

public interface LoanEligibilityService {
    EligibilityResult evaluateEligibility(Long loanRequestId);
    EligibilityResult getResultByRequest(Long requestId);
}
