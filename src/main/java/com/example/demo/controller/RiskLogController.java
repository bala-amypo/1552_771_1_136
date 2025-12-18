package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.RiskAssessmentLog;
import com.example.demo.service.RiskAssessmentLogService;

@RestController
@RequestMapping("/api/risk-logs")
public class RiskLogController {

    private final RiskAssessmentLogService riskAssessmentLogService;

    public RiskLogController(RiskAssessmentLogService riskAssessmentLogService) {
        this.riskAssessmentLogService = riskAssessmentLogService;
    }

    @PostMapping("/{loanRequestId}")
    public ResponseEntity<RiskAssessmentLog> assessRisk(
            @PathVariable Long loanRequestId) {

        return ResponseEntity.ok(
                riskAssessmentLogService.assessRisk(loanRequestId));
    }

    @GetMapping("/{loanRequestId}")
    public ResponseEntity<RiskAssessmentLog> getRiskLog(
            @PathVariable Long loanRequestId) {

        return ResponseEntity.ok(
                riskAssessmentLogService.getByLoanRequestId(loanRequestId));
    }
}
