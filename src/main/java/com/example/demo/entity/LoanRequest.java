package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "loan_requests") // Matches your DB table name
public class LoanRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Links to user table
    private User user;

    @Column(name = "requested_amount", nullable = false)
    private Double requestedAmount;

    @Column(name = "tenure_months", nullable = false)
    private Integer tenureMonths;

    private String purpose;

    @Column(nullable = false)
    private String status = "PENDING"; // Default value per rules

    @Column(name = "applied_at")
    private LocalDateTime appliedAt;

    @PrePersist
    protected void onCreate() {
        this.appliedAt = LocalDateTime.now(); // Auto-populates on creation
    }

    public LoanRequest() {}

    // Standard Getters and Setters
    public Long getId() { return id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Double getRequestedAmount() { return requestedAmount; }
    public void setRequestedAmount(Double requestedAmount) { this.requestedAmount = requestedAmount; }
    public Integer getTenureMonths() { return tenureMonths; }
    public void setTenureMonths(Integer tenureMonths) { this.tenureMonths = tenureMonths; }
    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}