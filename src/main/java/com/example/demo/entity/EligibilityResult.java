package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class EligibilityResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private LoanRequest loanRequest;

    private Boolean isEligible;
    private Double maxEligibleAmount;
    private Double estimatedEmi;
    private String riskLevel;
    private String rejectionReason;

    private LocalDateTime calculatedAt = LocalDateTime.now();

    public EligibilityResult() {}

    public void setLoanRequest(LoanRequest loanRequest) {
        this.loanRequest = loanRequest;
    }

    public void setIsEligible(Boolean isEligible) {
        this.isEligible = isEligible;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public void setMaxEligibleAmount(Double maxEligibleAmount) {
        this.maxEligibleAmount = maxEligibleAmount;
    }

    public void setEstimatedEmi(Double estimatedEmi) {
        this.estimatedEmi = estimatedEmi;
    }
}
