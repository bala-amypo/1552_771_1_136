package com.example.demo.controller;

import com.example.demo.entity.RiskAssessmentLog;
import com.example.demo.service.impl.RiskAssessmentLogServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/risk-logs")
public class RiskLogController {

    private final RiskAssessmentLogServiceImpl riskLogService;

    public RiskLogController(RiskAssessmentLogServiceImpl riskLogService) {
        this.riskLogService = riskLogService;
    }

    // Get all risk logs for a loan request
    @GetMapping("/{loanRequestId}")
    public ResponseEntity<List<RiskAssessmentLog>> getLogsByRequest(@PathVariable Long loanRequestId) {
        List<RiskAssessmentLog> logs = riskLogService.getLogsByRequest(loanRequestId);
        return ResponseEntity.ok(logs);
    }
}
