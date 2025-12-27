package com.example.demo.controller;

import com.example.demo.entity.RiskAssessment;
import com.example.demo.service.RiskAssessmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/risk-logs")
public class RiskLogController {

    private final RiskAssessmentService service;

    public RiskLogController(RiskAssessmentService service) {
        this.service = service;
    }

    @GetMapping("/{loanRequestId}")
    public ResponseEntity<RiskAssessment> getByLoanRequest(
            @PathVariable Long loanRequestId) {
        return ResponseEntity.ok(service.getByLoanRequestId(loanRequestId));
    }
}
