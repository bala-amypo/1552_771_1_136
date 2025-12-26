package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "risk_assessments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskAssessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "loan_request_id", nullable = false)
    private LoanRequest loanRequest;

    private Double dtiRatio;
    private Double riskScore;
    private String creditCheckStatus;
    private LocalDateTime assessmentTimestamp;

    @PrePersist
    protected void onCreate() {
        assessmentTimestamp = LocalDateTime.now();
    }
}