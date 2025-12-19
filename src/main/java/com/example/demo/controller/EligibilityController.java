package com.example.demo.controller;

import com.example.demo.entity.EligibilityResult;
import com.example.demo.service.LoanEligibilityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/eligibility")
@Tag(name = "eligibility-controller")
public class EligibilityController {

    private final LoanEligibilityService service;

    public EligibilityController(LoanEligibilityService service) {
        this.service = service;
    }

    @PostMapping("/evaluate/{loanRequestId}")
    public EligibilityResult evaluate(@PathVariable Long loanRequestId) {
        return service.evaluate(loanRequestId);
    }
}
