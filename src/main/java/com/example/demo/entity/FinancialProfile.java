package com.example.loan.entity;

import jakarta.persistence.*;

@Entity
public class FinancialProfile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double income;
    private Integer creditScore;

    @OneToOne
    private User user;
}
