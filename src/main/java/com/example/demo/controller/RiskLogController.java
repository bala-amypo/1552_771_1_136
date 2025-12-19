package com.example.demo.controller;

import com.example.demo.entity.RiskAssessmentLog;
import com.example.demo.service.RiskAssessmentLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/risk-logs")
public class RiskLogController {

    private final RiskAssessmentService service;

    public RiskLogController(RiskAssessmentService service) {
        this.service = service;
    }

    @PostMapping("/assess/{loanRequestId}")
    public ResponseEntity<RiskAssessmentLog> assess(@PathVariable Long loanRequestId) {
        return ResponseEntity.ok(service.assessRisk(loanRequestId));
    }

    @GetMapping("/{loanRequestId}")
    public ResponseEntity<RiskAssessmentLog> getLog(@PathVariable Long loanRequestId) {
        return ResponseEntity.ok(service.getByLoanRequestId(loanRequestId));
    }
}
