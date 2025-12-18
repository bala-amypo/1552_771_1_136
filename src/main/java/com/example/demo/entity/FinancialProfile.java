package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "financial_profiles", uniqueConstraints = @UniqueConstraint(columnNames = "user_id"))
public class FinancialProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double monthlyIncome;
    private Double monthlyExpenses;
    private Double existingLoanEmi;
    private int creditScore;
    private Double savingsBalance;
    private Instant lastUpdatedAt = Instant.now();

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public FinancialProfile() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getMonthlyIncome() { return monthlyIncome; }
    public void setMonthlyIncome(Double monthlyIncome) { this.monthlyIncome = monthlyIncome; }
    public Double getMonthlyExpenses() { return monthlyExpenses; }
    public void setMonthlyExpenses(Double monthlyExpenses) { this.monthlyExpenses = monthlyExpenses; }
    public Double getExistingLoanEmi() { return existingLoanEmi; }
    public void setExistingLoanEmi(Double existingLoanEmi) { this.existingLoanEmi = existingLoanEmi; }
    public int getCreditScore() { return creditScore; }
    public void setCreditScore(int creditScore) { this.creditScore = creditScore; }
    public Double getSavingsBalance() { return savingsBalance; }
    public void setSavingsBalance(Double savingsBalance) { this.savingsBalance = savingsBalance; }
    public Instant getLastUpdatedAt() { return lastUpdatedAt; }
    public void setLastUpdatedAt(Instant lastUpdatedAt) { this.lastUpdatedAt = lastUpdatedAt; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
