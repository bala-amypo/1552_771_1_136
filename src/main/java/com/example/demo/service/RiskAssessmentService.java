package com.example.demo.service;

import com.example.demo.entity.RiskAssessment;

public interface RiskAssessmentService {
    /**
     * Performs a risk assessment and logs the audit details.
     */
    RiskAssessment assessRisk(Long loanRequestId);

    /**
     * Retrieves the risk assessment log for a specific loan request.
     */
    RiskAssessment getByLoanRequestId(Long loanRequestId);
}