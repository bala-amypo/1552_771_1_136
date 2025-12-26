package com.example.demo.service;

import com.example.demo.entity.RiskAssessment;

public interface RiskAssessmentService {

    RiskAssessment assessRisk(Long loanRequestId);

    RiskAssessment getByLoanRequestId(Long loanRequestId);
}
