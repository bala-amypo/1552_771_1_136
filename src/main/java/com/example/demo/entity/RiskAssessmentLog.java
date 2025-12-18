package com.example.demo.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "risk_assessment_logs")
public class RiskAssessmentLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long loanRequestId;
    private Double dtiRatio;
    private String creditCheckStatus;
    private Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getLoanRequestId() { return loanRequestId; }
    public void setLoanRequestId(Long loanRequestId) { this.loanRequestId = loanRequestId; }

    public Double getDtiRatio() { return dtiRatio; }
    public void setDtiRatio(Double dtiRatio) { this.dtiRatio = dtiRatio; }

    public String getCreditCheckStatus() { return creditCheckStatus; }
    public void setCreditCheckStatus(String creditCheckStatus) { this.creditCheckStatus = creditCheckStatus; }

    public Timestamp getTimestamp() { return timestamp; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }
}
