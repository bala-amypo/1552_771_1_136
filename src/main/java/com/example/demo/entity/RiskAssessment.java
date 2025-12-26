package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
public class RiskAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private LoanRequest loanRequest;

    private Double riskScore;
    private Double dtiRatio;

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LoanRequest getLoanRequest() { return loanRequest; }
    public void setLoanRequest(LoanRequest loanRequest) { this.loanRequest = loanRequest; }

    public Double getRiskScore() { return riskScore; }
    public void setRiskScore(Double riskScore) { this.riskScore = riskScore; }

    public Double getDtiRatio() { return dtiRatio; }
    public void setDtiRatio(Double dtiRatio) { this.dtiRatio = dtiRatio; }
}
