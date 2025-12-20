package com.example.demo.controller;

import com.example.demo.dto.LoanDtos;
import com.example.demo.entity.RiskAssessmentLog;
import com.example.demo.service.RiskAssessmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/risk-logs")
public class RiskLogController {

    private final RiskAssessmentService riskService;

    public RiskLogController(RiskAssessmentService riskService) {
        this.riskService = riskService;
    }

    @GetMapping("/{loanRequestId}")
    public ResponseEntity<LoanDtos.RiskAssessmentLogDto> getRiskLog(@PathVariable Long loanRequestId) {
        RiskAssessmentLog log = riskService.getByLoanRequestId(loanRequestId);
        LoanDtos.RiskAssessmentLogDto dto = new LoanDtos.RiskAssessmentLogDto();
        dto.setId(log.getId());
        dto.setLoanRequestId(log.getLoanRequestId());
        dto.setDtiRatio(log.getDtiRatio());
        dto.setCreditCheckStatus(log.getCreditCheckStatus());
        dto.setTimestamp(log.getTimestamp());
        return ResponseEntity.ok(dto);
    }
}
