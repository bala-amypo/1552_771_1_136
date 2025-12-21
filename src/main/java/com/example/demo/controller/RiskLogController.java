package com.example.demo.controller;

import com.example.demo.entity.RiskAssessmentLog;
import com.example.demo.service.RiskAssessmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/risk-logs")
public class RiskLogController {
    private final RiskAssessmentService riskService;

    public RiskLogController(RiskAssessmentService riskService) {
        this.riskService = riskService;
    }

    @PostMapping("/assess/{loanRequestId}")
    public ResponseEntity<RiskAssessmentLog> assess(@PathVariable Long loanRequestId) {
        return ResponseEntity.ok(riskService.assessRisk(loanRequestId));
    }

    @GetMapping("/{loanRequestId}")
    public ResponseEntity<RiskAssessmentLog> getLogs(@PathVariable Long loanRequestId) {
        return ResponseEntity.ok(riskService.getByLoanRequestId(loanRequestId));
    }
}