package com.example.loan.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
public class EligibilityResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private LoanRequest loanRequest;

    private Boolean isEligible;
    private Double maxEligibleAmount;
    private Double estimatedEmi;
    private String riskLevel;
    private String rejectionReason;

    private Timestamp calculatedAt = new Timestamp(System.currentTimeMillis());

    // getters and setters
}
