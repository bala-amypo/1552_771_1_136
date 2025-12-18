package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
public class FinancialProfile {


@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;


@OneToOne
@JoinColumn(name = "user_id", unique = true, nullable = false)
private User user;


private Double monthlyIncome;
private Double monthlyExpenses;
private Double existingLoanEmi;
private Integer creditScore;
private Double savingsBalance;
private LocalDateTime lastUpdatedAt;


@PrePersist
@PreUpdate
void updateTime() {
this.lastUpdatedAt = LocalDateTime.now();
}


public FinancialProfile() {}


// getters and setters
}