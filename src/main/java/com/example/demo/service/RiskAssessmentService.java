package com.example.demo.service;
import com.example.demo.entity.RiskAssessmentLog;
import java.util.List;

public interface RiskAssessmentService {
    RiskAssessmentLog assessRisk(Long loanRequestId);
    List<RiskAssessmentLog> getByLoanRequestId(Long loanRequestId);
}