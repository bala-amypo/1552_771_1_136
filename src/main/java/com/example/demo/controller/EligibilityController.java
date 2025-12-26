package com.example.demo.controller;

import com.example.demo.dto.EligibilityResultDto;
import com.example.demo.service.EligibilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/eligibility")
public class EligibilityController {

    private final EligibilityService service;

    public EligibilityController(EligibilityService service) {
        this.service = service;
    }

    // POST /api/eligibility/evaluate/{loanRequestId}
    @PostMapping("/evaluate/{loanRequestId}")
    public ResponseEntity<EligibilityResultDto> evaluate(
            @PathVariable Long loanRequestId) {
        return ResponseEntity.ok(service.evaluate(loanRequestId));
    }

    // GET /api/eligibility/result/{loanRequestId}
    @GetMapping("/result/{loanRequestId}")
    public ResponseEntity<EligibilityResultDto> getResult(
            @PathVariable Long loanRequestId) {
        return ResponseEntity.ok(service.getResult(loanRequestId));
    }
}
