package com.example.demo.controller;

import com.example.demo.dto.RiskLogDto;
import com.example.demo.service.RiskLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/risk-logs")
public class RiskLogController {

    private final RiskLogService service;

    public RiskLogController(RiskLogService service) {
        this.service = service;
    }

    // GET /api/risk-logs/{loanRequestId}
    @GetMapping("/{loanRequestId}")
    public ResponseEntity<List<RiskLogDto>> getRiskLogs(
            @PathVariable Long loanRequestId) {
        return ResponseEntity.ok(service.getByLoanRequestId(loanRequestId));
    }
}
