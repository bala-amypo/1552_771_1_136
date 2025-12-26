package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

public class LoanDtos {

    @Data
    public static class LoanRequestDto {
        private Long id;
        private Long userId;
        private Double requestedAmount;
        private Integer tenureMonths;
        private String purpose;
        private String status;
        private LocalDateTime appliedAt;
    }

    @Data
    public static class FinancialProfileDto {
        private Long id;
        private Long userId;
        private Double monthlyIncome;
        private Double monthlyExpenses;
        private Double existingLoanEmi;
        private Integer creditScore;
        private Double savingsBalance;
        private LocalDateTime lastUpdatedAt;
    }
}