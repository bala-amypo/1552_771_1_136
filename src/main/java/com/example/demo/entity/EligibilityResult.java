package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "eligibility_results")
public class EligibilityResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "loan_request_id", nullable = false, unique = true)
    private LoanRequest loanRequest;

    @Column(nullable = false)
    private Boolean isEligible;

    @Column(name = "max_eligible_amount", nullable = false)
    private Double maxEligibleAmount;

    @Column(name = "estimated_emi", nullable = false)
    private Double estimatedEmi;

    @Column(name = "risk_level", nullable = false)
    private String riskLevel;

    private String rejectionReason;

    @Column(name = "calculated_at")
    private LocalDateTime calculatedAt;

    @PrePersist
    protected void onCreate() {
        this.calculatedAt = LocalDateTime.now();
    }

    public EligibilityResult() {}

    // Getters and Setters
    public Long getId() { return id; }
    public LoanRequest getLoanRequest() { return loanRequest; }
    public void setLoanRequest(LoanRequest loanRequest) { this.loanRequest = loanRequest; }
    public Boolean getIsEligible() { return isEligible; }
    public void setIsEligible(Boolean eligible) { isEligible = eligible; }
    public Double getMaxEligibleAmount() { return maxEligibleAmount; }
    public void setMaxEligibleAmount(Double maxEligibleAmount) { this.maxEligibleAmount = maxEligibleAmount; }
    public Double getEstimatedEmi() { return estimatedEmi; }
    public void setEstimatedEmi(Double emi) { this.estimatedEmi = emi; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String reason) { this.rejectionReason = reason; }
}