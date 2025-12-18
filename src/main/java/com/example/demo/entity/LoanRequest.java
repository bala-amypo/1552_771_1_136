package com.example.loan.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
public class Loan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double amount;
    private Integer tenure;
    private String status;
    private Timestamp appliedAt = new Timestamp(System.currentTimeMillis());

    @ManyToOne
    private User user;
}
