package com.example.demo.service;

import com.example.demo.entity.RiskAssessmentLog;

import java.util.List;

public interface RiskAssessmentService {

    List<RiskAssessmentLog> getByLoanRequestId(Long loanRequestId);

    RiskAssessmentLog save(RiskAssessmentLog log);
}
