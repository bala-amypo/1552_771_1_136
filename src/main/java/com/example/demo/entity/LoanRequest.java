package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "loan_requests")
public class LoanRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double requestedAmount;
    private int tenureMonths;
    private String purpose;
    private String status;
    private Instant appliedAt = Instant.now();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public LoanRequest() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getRequestedAmount() { return requestedAmount; }
    public void setRequestedAmount(Double requestedAmount) { this.requestedAmount = requestedAmount; }
    public int getTenureMonths() { return tenureMonths; }
    public void setTenureMonths(int tenureMonths) { this.tenureMonths = tenureMonths; }
    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Instant getAppliedAt() { return appliedAt; }
    public void setAppliedAt(Instant appliedAt) { this.appliedAt = appliedAt; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
