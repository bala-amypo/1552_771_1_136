package com.example.demo.controller;

import com.example.demo.entity.RiskAssessmentLog;
import com.example.demo.service.RiskAssessmentLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/risk-logs")
@Tag(name = "Risk Logs", description = "Endpoints for loan risk assessment logs")
public class RiskLogController {

    private final RiskAssessmentLogService service;

    public RiskLogController(RiskAssessmentLogService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create a new risk assessment log")
    public RiskAssessmentLog createLog(@RequestBody RiskAssessmentLog log) {
        return service.logAssessment(log);
    }

    @GetMapping("/{loanRequestId}")
    @Operation(summary = "Get all risk logs by loan request ID")
    public List<RiskAssessmentLog> getLogs(@PathVariable Long loanRequestId) {
        return service.getLogsByRequest(loanRequestId);
    }
}
