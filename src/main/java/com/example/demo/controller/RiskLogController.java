package com.example.demo.controller;

import com.example.demo.entity.RiskAssessmentLog;
import com.example.demo.service.RiskAssessmentLogService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/risk-logs")
public class RiskLogController {

    private final RiskAssessmentLogService service;

    public RiskLogController(RiskAssessmentLogService service) { this.service = service; }

    @GetMapping("/{loanRequestId}")
    public List<RiskAssessmentLog> getLogs(@PathVariable Long loanRequestId) { return service.getLogsByRequest(loanRequestId); }
}
