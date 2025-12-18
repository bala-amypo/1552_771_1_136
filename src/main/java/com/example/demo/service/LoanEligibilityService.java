package com.example.demo.service;

import com.example.demo.entity.LoanRequest;
import com.example.demo.entity.EligibilityResult;

public interface LoanEligibilityService {
    EligibilityResult calculateEligibility(LoanRequest request);
}
