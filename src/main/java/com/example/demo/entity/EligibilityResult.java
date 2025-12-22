package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "eligibility_result")
@JsonPropertyOrder({ "id", "loanRequest", "isEligible", "maxEligibleAmount", "estimatedEmi", "riskLevel", "rejectionReason", "calculatedAt" })
public class EligibilityResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "loan_request_id", referencedColumnName = "id", nullable = false)
    private LoanRequest loanRequest;

    @Column(name = "is_eligible")
    private Boolean isEligible;

    @Column(name = "max_eligible_amount")
    private Double maxEligibleAmount;

    @Column(name = "estimated_emi")
    private Double estimatedEmi;

    @Column(name = "risk_level")
    private String riskLevel;

    @Column(name = "rejection_reason")
    private String rejectionReason;

    @Column(name = "calculated_at")
    private LocalDateTime calculatedAt;

    @PrePersist
    protected void onCreate() {
        this.calculatedAt = LocalDateTime.now();
    }

    public EligibilityResult() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LoanRequest getLoanRequest() {
        return loanRequest;
    }

    public void setLoanRequest(LoanRequest loanRequest) {
        this.loanRequest = loanRequest;
    }

    public Boolean getIsEligible() {
        return isEligible;
    }

    public void setIsEligible(Boolean isEligible) {
        this.isEligible = isEligible;
    }

    public Double getMaxEligibleAmount() {
        return maxEligibleAmount;
    }

    public void setMaxEligibleAmount(Double maxEligibleAmount) {
        this.maxEligibleAmount = maxEligibleAmount;
    }

    public Double getEstimatedEmi() {
        return estimatedEmi;
    }

    public void setEstimatedEmi(Double estimatedEmi) {
        this.estimatedEmi = estimatedEmi;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public LocalDateTime getCalculatedAt() {
        return calculatedAt;
    }

    public void setCalculatedAt(LocalDateTime calculatedAt) {
        this.calculatedAt = calculatedAt;
    }
}

