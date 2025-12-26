package com.example.demo.dto;

import java.time.LocalDateTime;

public class LoanDtos {

    // Loan Request DTO
    public static class LoanRequestDto {
        private Long userId;
        private Double requestedAmount;
        private Integer tenureMonths;

        public LoanRequestDto() {}

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        public Double getRequestedAmount() { return requestedAmount; }
        public void setRequestedAmount(Double requestedAmount) { this.requestedAmount = requestedAmount; }

        public Integer getTenureMonths() { return tenureMonths; }
        public void setTenureMonths(Integer tenureMonths) { this.tenureMonths = tenureMonths; }
    }

    // Loan Eligibility Response DTO
    public static class EligibilityResultDto {
        private Boolean isEligible;
        private Double maxEligibleAmount;
        private Double estimatedEmi;
        private String riskLevel;
        private String rejectionReason;
        private LocalDateTime calculatedAt;

        public EligibilityResultDto() {}

        public Boolean getIsEligible() { return isEligible; }
        public void setIsEligible(Boolean isEligible) { this.isEligible = isEligible; }

        public Double getMaxEligibleAmount() { return maxEligibleAmount; }
        public void setMaxEligibleAmount(Double maxEligibleAmount) { this.maxEligibleAmount = maxEligibleAmount; }

        public Double getEstimatedEmi() { return estimatedEmi; }
        public void setEstimatedEmi(Double estimatedEmi) { this.estimatedEmi = estimatedEmi; }

        public String getRiskLevel() { return riskLevel; }
        public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }

        public String getRejectionReason() { return rejectionReason; }
        public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

        public LocalDateTime getCalculatedAt() { return calculatedAt; }
        public void setCalculatedAt(LocalDateTime calculatedAt) { this.calculatedAt = calculatedAt; }
    }

}
