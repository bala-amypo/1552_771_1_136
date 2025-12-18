package com.example.loan.controller;

import com.example.loan.entity.EligibilityResult;
import com.example.loan.service.LoanEligibilityService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/eligibility")
public class EligibilityController {

    private final LoanEligibilityService service;

    public EligibilityController(LoanEligibilityService service) { this.service = service; }

    @PostMapping("/evaluate/{loanRequestId}")
    public EligibilityResult evaluate(@PathVariable Long loanRequestId) { return service.evaluateEligibility(loanRequestId); }

    @GetMapping("/result/{loanRequestId}")
    public EligibilityResult getResult(@PathVariable Long loanRequestId) { return service.getResultByRequest(loanRequestId); }
}
