// src/main/java/com/example/demo/entity/FinancialProfile.java
package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "financial_profiles")
public class FinancialProfile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Double monthlyIncome;

    @Column(nullable = false)
    private Double monthlyExpenses;

    @Column
    private Double existingLoanEmi;

    @Column(nullable = false)
    private Integer creditScore;

    @Column(nullable = false)
    private Double savingsBalance;

    @Column(nullable = false)
    private LocalDateTime lastUpdatedAt;

    public FinancialProfile() {}

    @PrePersist @PreUpdate
    public void onUpdate() {
        lastUpdatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public Double getMonthlyIncome() { return monthlyIncome; }
    public Double getMonthlyExpenses() { return monthlyExpenses; }
    public Double getExistingLoanEmi() { return existingLoanEmi == null ? 0.0 : existingLoanEmi; }
    public Integer getCreditScore() { return creditScore; }
    public Double getSavingsBalance() { return savingsBalance; }
    public LocalDateTime getLastUpdatedAt() { return lastUpdatedAt; }
    public void setId(Long id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setMonthlyIncome(Double monthlyIncome) { this.monthlyIncome = monthlyIncome; }
    public void setMonthlyExpenses(Double monthlyExpenses) { this.monthlyExpenses = monthlyExpenses; }
    public void setExistingLoanEmi(Double existingLoanEmi) { this.existingLoanEmi = existingLoanEmi; }
    public void setCreditScore(Integer creditScore) { this.creditScore = creditScore; }
    public void setSavingsBalance(Double savingsBalance) { this.savingsBalance = savingsBalance; }
    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) { this.lastUpdatedAt = lastUpdatedAt; }
}
