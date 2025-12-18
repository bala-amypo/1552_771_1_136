package com.example.demo.controller;

import com.example.demo.entity.LoanRequest;
import com.example.demo.entity.EligibilityResult;
import com.example.demo.service.LoanEligibilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eligibility")
public class EligibilityController {

    private final LoanEligibilityService loanEligibilityService;

    public EligibilityController(LoanEligibilityService loanEligibilityService) {
        this.loanEligibilityService = loanEligibilityService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<EligibilityResult> calculate(@RequestBody LoanRequest request) {
        return ResponseEntity.ok(loanEligibilityService.calculateEligibility(request));
    }
}
