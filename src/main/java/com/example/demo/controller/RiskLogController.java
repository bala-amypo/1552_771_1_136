package com.example.demo.controller;

import com.example.demo.entity.RiskAssessmentLog;
import com.example.demo.service.RiskAssessmentLogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/risk-logs")
public class RiskLogController {

    @Autowired
    private RiskAssessmentLogService riskAssessmentLogService;

    @PostMapping
    public RiskAssessmentLog createLog(@RequestBody RiskAssessmentLog log) {
        return riskAssessmentLogService.logAssessment(log);
    }

    @GetMapping("/{loanRequestId}")
    public List<RiskAssessmentLog> getLogs(@PathVariable Long loanRequestId) {
        return riskAssessmentLogService.getLogsByRequest(loanRequestId);
    }
}
