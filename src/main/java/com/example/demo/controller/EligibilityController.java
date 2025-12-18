package com.example.demo.controller;

import org.springframework.web.bind.annotation.RestController;
import com.example.demo.service.LoanEligibilityService;

@RestController
public class EligibilityController {

    private final LoanEligibilityService loanEligibilityService;

    public EligibilityController(LoanEligibilityService loanEligibilityService) {
        this.loanEligibilityService = loanEligibilityService;
    }

    // Your endpoints here
}
