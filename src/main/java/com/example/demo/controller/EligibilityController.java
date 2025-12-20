package com.example.demo.controller;

import com.example.demo.dto.LoanDtos;
import com.example.demo.entity.EligibilityResult;
import com.example.demo.service.LoanEligibilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/eligibility")
public class EligibilityController {

    private final LoanEligibilityService eligibilityService;

    public EligibilityController(LoanEligibilityService eligibilityService) {
        this.eligibilityService = eligibilityService;
    }

    @PostMapping("/evaluate/{loanRequestId}")
    public ResponseEntity<LoanDtos.EligibilityResultDto> evaluate(@PathVariable Long loanRequestId) {
        EligibilityResult result = eligibilityService.evaluateEligibility(loanRequestId);
        return ResponseEntity.ok(mapToDto(result));
    }

    @GetMapping("/result/{loanRequestId}")
    public ResponseEntity<LoanDtos.EligibilityResultDto> getResult(@PathVariable Long loanRequestId) {
        EligibilityResult result = eligibilityService.getByLoanRequestId(loanRequestId);
        return ResponseEntity.ok(mapToDto(result));
    }

    private LoanDtos.EligibilityResultDto mapToDto(EligibilityResult result) {
        LoanDtos.EligibilityResultDto dto = new LoanDtos.EligibilityResultDto();
        dto.setId(result.getId());
        dto.setLoanRequestId(result.getLoanRequest().getId());
        dto.setIsEligible(result.getIsEligible());
        dto.setMaxEligibleAmount(result.getMaxEligibleAmount());
        dto.setEstimatedEmi(result.getEstimatedEmi());
        dto.setRiskLevel(result.getRiskLevel());
        dto.setRejectionReason(result.getRejectionReason());
        dto.setCalculatedAt(result.getCalculatedAt());
        return dto;
    }
}
