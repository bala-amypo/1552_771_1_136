package com.example.loan.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;

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

    private Timestamp lastUpdatedAt = new Timestamp(System.currentTimeMillis());

    // getters and setters
}
