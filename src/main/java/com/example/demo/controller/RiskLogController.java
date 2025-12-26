package com.example.demo.controller;

import com.example.demo.entity.RiskAssessmentLog;
import com.example.demo.service.RiskAssessmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/risk-logs")
public class RiskLogController {

    private final RiskAssessmentService riskService;

    public RiskLogController(RiskAssessmentService riskService) {
        this.riskService = riskService;
    }

    @GetMapping("/{loanRequestId}")
    public ResponseEntity<List<RiskAssessmentLog>> getLogsByLoanRequest(@PathVariable Long loanRequestId) {
        List<RiskAssessmentLog> logs = riskService.getByLoanRequestId(loanRequestId);
        return ResponseEntity.ok(logs);
    }
}
