package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class RiskAssessment {

    @Id
    @GeneratedValue
    private Long id;

    private Long loanRequestId;
    private Double dtiRatio;
    private Double riskScore;
    private String creditCheckStatus;
    private Instant timestamp = Instant.now();

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getLoanRequestId() { return loanRequestId; }
    public void setLoanRequestId(Long loanRequestId) { this.loanRequestId = loanRequestId; }

    public Double getDtiRatio() { return dtiRatio; }
    public void setDtiRatio(Double dtiRatio) { this.dtiRatio = dtiRatio; }

    public Double getRiskScore() { return riskScore; }
    public void setRiskScore(Double riskScore) { this.riskScore = riskScore; }

    public String getCreditCheckStatus() { return creditCheckStatus; }
    public void setCreditCheckStatus(String creditCheckStatus) { this.creditCheckStatus = creditCheckStatus; }
}
