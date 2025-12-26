package com.example.demo.service;

import com.example.demo.entity.RiskAssessmentLog;

public interface RiskAssessmentService {

    RiskAssessment assessRisk(Long loanRequestId);

    RiskAssessment getByLoanRequestId(Long loanRequestId);
}
