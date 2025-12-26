package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class EligibilityResult {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private LoanRequest loanRequest;

    private Boolean isEligible;
    private Double maxEligibleAmount;
    private Double estimatedEmi;
    private String riskLevel;
    private String rejectionReason;
    private Instant calculatedAt = Instant.now();

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LoanRequest getLoanRequest() { return loanRequest; }
    public void setLoanRequest(LoanRequest loanRequest) { this.loanRequest = loanRequest; }

    public Boolean getIsEligible() { return isEligible; }
    public void setIsEligible(Boolean eligible) { isEligible = eligible; }

    public Double getMaxEligibleAmount() { return maxEligibleAmount; }
    public void setMaxEligibleAmount(Double maxEligibleAmount) { this.maxEligibleAmount = maxEligibleAmount; }

    public Double getEstimatedEmi() { return estimatedEmi; }
    public void setEstimatedEmi(Double estimatedEmi) { this.estimatedEmi = estimatedEmi; }

    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
}
