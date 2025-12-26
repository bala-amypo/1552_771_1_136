package com.example.demo.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
public class EligibilityResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private LoanRequest loanRequest;

    private boolean isEligible;
    private double estimatedEmi;
    private String riskLevel;
    private String rejectionReason;
    private LocalDateTime calculatedAt;

    // Setters
    public void setLoanRequest(LoanRequest loanRequest) { this.loanRequest = loanRequest; }
    public void setIsEligible(boolean isEligible) { this.isEligible = isEligible; }
    public void setEstimatedEmi(double estimatedEmi) { this.estimatedEmi = estimatedEmi; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
    public void setCalculatedAt(LocalDateTime calculatedAt) { this.calculatedAt = calculatedAt; }

    // Getters (optional, but recommended)
}
