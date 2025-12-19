package com.example.demo.service;

import com.example.demo.entity.RiskAssessmentLog;

public interface RiskAssessmentLogService {
    RiskAssessmentLog assessRisk(Long loanRequestId);
    RiskAssessmentLog getByLoanRequestId(Long loanRequestId);
}
