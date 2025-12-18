package com.example.demo.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "loan_requests")
public class LoanRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private Double requestedAmount;
    private Integer tenureMonths;
    private String purpose;
    private String status = "PENDING";
    private Timestamp appliedAt = new Timestamp(System.currentTimeMillis());
    // Getters and Setters
}
