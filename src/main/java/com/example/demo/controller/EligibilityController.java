package com.example.demo.controller;

import com.example.demo.entity.EligibilityResult;
import com.example.demo.service.LoanEligibilityService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/eligibility")
public class EligibilityController {

    private final LoanEligibilityService loanEligibilityService;

    public EligibilityController(LoanEligibilityService loanEligibilityService) {
        this.loanEligibilityService = loanEligibilityService;
    }

    @PostMapping("/evaluate/{loanRequestId}")
    public EligibilityResult evaluate(@PathVariable Long loanRequestId) {
        return loanEligibilityService.evaluateEligibility(loanRequestId);
    }

    @GetMapping("/result/{loanRequestId}")
    public EligibilityResult getResult(@PathVariable Long loanRequestId) {
        return loanEligibilityService.getByLoanRequestId(loanRequestId);
    }
}
