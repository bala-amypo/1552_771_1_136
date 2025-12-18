package com.example.demo.entity;


import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
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
private LocalDateTime appliedAt;


@PrePersist
void onCreate() {
this.appliedAt = LocalDateTime.now();
}


public LoanRequest() {}


// getters and setters
}