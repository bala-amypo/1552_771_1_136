package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.RiskAssessmentLog;
import com.example.demo.service.RiskAssessmentLogService;

@RestController
@RequestMapping("/api/risk-logs")
public class RiskLogController {

    private final RiskAssessmentService riskAssessmentService;

    public RiskLogController(RiskAssessmentService riskAssessmentService) {
        this.riskAssessmentService = riskAssessmentService;
    }

    @GetMapping("/{loanRequestId}")
    public ResponseEntity<RiskAssessmentLog> getRiskLog(
            @PathVariable Long loanRequestId) {
        return ResponseEntity.ok(
                riskAssessmentService.getByLoanRequestId(loanRequestId));
    }
}
