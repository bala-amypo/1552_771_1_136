package com.example.demo.entity;


import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
public class EligibilityResult {


@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;


@OneToOne
@JoinColumn(name = "loan_request_id", unique = true)
private LoanRequest loanRequest;


private Boolean isEligible;
private Double maxEligibleAmount;
private Double estimatedEmi;
private String riskLevel;
private String rejectionReason;
private LocalDateTime calculatedAt;


@PrePersist
void onCreate() {
this.calculatedAt = LocalDateTime.now();
}


public EligibilityResult() {}


// getters and setters
}