package com.example.demo.dto;

import java.time.LocalDateTime;

public class LoanDtos {

    // Inner class for LoanRequest DTO
    public static class LoanRequestDto {
        private Long id;
        private Long userId;
        private Double requestedAmount;
        private Integer tenureMonths;
        private String purpose;
        private String status;
        private LocalDateTime appliedAt;

        public LoanRequestDto() {}

        public LoanRequestDto(Long id, Long userId, Double requestedAmount,
                              Integer tenureMonths, String purpose,
                              String status, LocalDateTime appliedAt) {
            this.id = id;
            this.userId = userId;
            this.requestedAmount = requestedAmount;
            this.tenureMonths = tenureMonths;
            this.purpose = purpose;
            this.status = status;
            this.appliedAt = appliedAt;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        public Double getRequestedAmount() { return requestedAmount; }
        public void setRequestedAmount(Double requestedAmount) { this.requestedAmount = requestedAmount; }

        public Integer getTenureMonths() { return tenureMonths; }
        public void setTenureMonths(Integer tenureMonths) { this.tenureMonths = tenureMonths; }

        public String getPurpose() { return purpose; }
        public void setPurpose(String purpose) { this.purpose = purpose; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public LocalDateTime getAppliedAt() { return appliedAt; }
        public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }
    }

    // Inner class for FinancialProfile DTO
    public static class FinancialProfileDto {
        private Long id;
        private Long userId;
        private Double monthlyIncome;
        private Double monthlyExpenses;
        private Double existingLoanEmi;
        private Integer creditScore;
        private Double savingsBalance;
        private LocalDateTime lastUpdatedAt;

        public FinancialProfileDto() {}

        public FinancialProfileDto(Long id, Long userId, Double monthlyIncome,
                                   Double monthlyExpenses, Double existingLoanEmi,
                                   Integer creditScore, Double savingsBalance,
                                   LocalDateTime lastUpdatedAt) {
            this.id = id;
            this.userId = userId;
            this.monthlyIncome = monthlyIncome;
            this.monthlyExpenses = monthlyExpenses;
            this.existingLoanEmi = existingLoanEmi;
            this.creditScore = creditScore;
            this.savingsBalance = savingsBalance;
            this.lastUpdatedAt = lastUpdatedAt;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        public Double getMonthlyIncome() { return monthlyIncome; }
        public void setMonthlyIncome(Double monthlyIncome) { this.monthlyIncome = monthlyIncome; }

        public Double getMonthlyExpenses() { return monthlyExpenses; }
        public void setMonthlyExpenses(Double monthlyExpenses) { this.monthlyExpenses = monthlyExpenses; }

        public Double getExistingLoanEmi() { return existingLoanEmi; }
        public void setExistingLoanEmi(Double existingLoanEmi) { this.existingLoanEmi = existingLoanEmi; }

        public Integer getCreditScore() { return creditScore; }
        public void setCreditScore(Integer creditScore) { this.creditScore = creditScore; }

        public Double getSavingsBalance() { return savingsBalance; }
        public void setSavingsBalance(Double savingsBalance) { this.savingsBalance = savingsBalance; }

        public LocalDateTime getLastUpdatedAt() { return lastUpdatedAt; }
        public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) { this.lastUpdatedAt = lastUpdatedAt; }
    }
}
