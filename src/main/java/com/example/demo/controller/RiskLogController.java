package com.example.demo.controller;

import com.example.demo.entity.RiskAssessmentLog;
import com.example.demo.service.RiskAssessmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/risk-logs")
@Tag(name = "risk-log-controller")
public class RiskLogController {

    private final RiskAssessmentService service;

    public RiskLogController(RiskAssessmentService service) {
        this.service = service;
    }

    @GetMapping("/{loanRequestId}")
    public List<RiskAssessmentLog> getLogs(@PathVariable Long loanRequestId) {
        return service.getLogs(loanRequestId);
    }
}
