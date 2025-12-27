package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class FinancialProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    private Double monthlyIncome;
    private Double monthlyExpenses;
    private Double existingLoanEmi;
    private Integer creditScore;
    private Double savingsBalance;

    private Instant lastUpdatedAt;

    // This method is triggered by Hibernate in production, 
    // but must be called manually in Mockito unit tests.
    @PrePersist
    @PreUpdate
    public void touch() {
        this.lastUpdatedAt = Instant.now();
    }

    // Getters and Setters...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Instant getLastUpdatedAt() { return lastUpdatedAt; }
    // ... (rest of the getters/setters)
}