package com.example.loan.entity;

import jakarta.persistence.*;

@Entity
public class Eligibility {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean eligible;
    private String riskLevel;

    @OneToOne
    private Loan loan;
}
