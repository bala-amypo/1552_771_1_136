package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "risk_assessment_logs")
public class RiskAssessmentLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double dtiRatio;
    private String creditCheckStatus;
    private Instant timestamp = Instant.now();

    @ManyToOne
    @JoinColumn(name = "loan_request_id")
    private LoanRequest loanRequest;

    public RiskAssessmentLog() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getDtiRatio() { return dtiRatio; }
    public void setDtiRatio(Double dtiRatio) { this.dtiRatio = dtiRatio; }
    public String getCreditCheckStatus() { return creditCheckStatus; }
    public void setCreditCheckStatus(String creditCheckStatus) { this.creditCheckStatus = creditCheckStatus; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
    public LoanRequest getLoanRequest() { return loanRequest; }
    public void setLoanRequest(LoanRequest loanRequest) { this.loanRequest = loanRequest; }
}
