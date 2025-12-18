package com.example.loan.service;

import com.example.loan.entity.RiskAssessmentLog;
import java.util.List;

public interface RiskAssessmentLogService {
    void logAssessment(RiskAssessmentLog log);
    List<RiskAssessmentLog> getLogsByRequest(Long requestId);
}
