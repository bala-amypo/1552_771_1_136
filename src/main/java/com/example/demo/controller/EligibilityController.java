package com.example.demo.controller;

import com.example.demo.entity.EligibilityResult;
import com.example.demo.service.LoanEligibilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/eligibility")
@Tag(name = "Eligibility", description = "Endpoints for evaluating loan eligibility")
public class EligibilityController {

    private final LoanEligibilityService service;

    public EligibilityController(LoanEligibilityService service) {
        this.service = service;
    }

    @PostMapping("/evaluate/{loanRequestId}")
    @Operation(summary = "Evaluate eligibility for a loan request")
    public EligibilityResult evaluate(@PathVariable Long loanRequestId) {
        return service.evaluateEligibility(loanRequestId);
    }

    @GetMapping("/result/{loanRequestId}")
    @Operation(summary = "Get eligibility result by loan request ID")
    public EligibilityResult getResult(@PathVariable Long loanRequestId) {
        return service.getResultByRequest(loanRequestId);
    }
}
