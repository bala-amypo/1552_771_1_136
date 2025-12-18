package com.example.demo.service.impl;

import org.springframework.stereotype.Service;
import com.example.demo.entity.LoanRequest;
import com.example.demo.service.LoanEligibilityService;

@Service  // <-- very important, otherwise Spring cannot create a bean
public class LoanEligibilityServiceImpl implements LoanEligibilityService {

    @Override
    public boolean isEligible(LoanRequest loanRequest) {
        // Your eligibility logic here
        return true; // example
    }
}
