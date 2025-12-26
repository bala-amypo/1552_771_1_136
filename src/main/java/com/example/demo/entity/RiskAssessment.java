// src/main/java/com/example/demo/entity/RiskAssessment.java
package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "risk_assessments")
public class RiskAssessment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long loanRequestId;

    @Column(nullable = false)
    private Double dtiRatio;

    @Column(nullable = false)
    private String creditCheckStatus;

    @Column(nullable = false)
    private Double riskScore;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public RiskAssessment() {}

    @PrePersist
    public void onCreate() { timestamp = LocalDateTime.now(); }

    public Long getId() { return id; }
    public Long getLoanRequestId() { return loanRequestId; }
    public Double getDtiRatio() { return dtiRatio; }
    public String getCreditCheckStatus() { return creditCheckStatus; }
    public Double getRiskScore() { return riskScore; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setId(Long id) { this.id = id; }
    public void setLoanRequestId(Long loanRequestId) { this.loanRequestId = loanRequestId; }
    public void setDtiRatio(Double dtiRatio) { this.dtiRatio = dtiRatio; }
    public void setCreditCheckStatus(String creditCheckStatus) { this.creditCheckStatus = creditCheckStatus; }
    public void setRiskScore(Double riskScore) { this.riskScore = riskScore; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
