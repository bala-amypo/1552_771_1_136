package com.example.demo.controller;

import com.example.demo.entity.RiskAssessmentLog;
import com.example.demo.service.RiskAssessmentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/risk-logs")
public class RiskLogController {

    private final RiskAssessmentService riskAssessmentService;

    public RiskLogController(RiskAssessmentService riskAssessmentService) {
        this.riskAssessmentService = riskAssessmentService;
    }

    @PostMapping("/{loanRequestId}")
    public RiskAssessmentLog assessRisk(@PathVariable Long loanRequestId) {
        return riskAssessmentService.assessRisk(loanRequestId);
    }

    @GetMapping("/{loanRequestId}")
    public RiskAssessmentLog getRiskLog(@PathVariable Long loanRequestId) {
        return riskAssessmentService.getByLoanRequestId(loanRequestId);
    }
}
