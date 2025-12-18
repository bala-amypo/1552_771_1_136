package com.example.demo.entity;


import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
public class RiskAssessmentLog {


@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;


private Long loanRequestId;
private Double dtiRatio;
private String creditCheckStatus;
private LocalDateTime timestamp;


@PrePersist
void onCreate() {
this.timestamp = LocalDateTime.now();
}


public RiskAssessmentLog() {}


// getters and setters
}