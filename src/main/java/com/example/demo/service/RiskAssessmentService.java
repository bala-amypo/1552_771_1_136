package com.example.demo.service;

import com.example.demo.entity.RiskAssessmentLog;

public interface RiskAssessmentService {

    /**
     * Assess risk for a loan request
     * @param loanRequestId loan request id
     * @return RiskAssessmentLog
     */
    RiskAssessmentLog assessRisk(Long loanRequestId);

    /**
     * Get risk assessment log by loan request ID
     * @param loanRequestId loan request id
     * @return RiskAssessmentLog
     */
    RiskAssessmentLog getByLoanRequestId(Long loanRequestId);
}
