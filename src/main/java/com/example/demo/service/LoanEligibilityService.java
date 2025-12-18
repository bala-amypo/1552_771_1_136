package com.example.demo.service;

import com.example.demo.entity.LoanRequest;

public interface LoanEligibilityService {
    boolean isEligible(LoanRequest loanRequest);
}
