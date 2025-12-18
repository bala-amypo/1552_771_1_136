package com.example.demo.controller;

import com.example.demo.entity.EligibilityResult;
import com.example.demo.service.LoanEligibilityService;
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
