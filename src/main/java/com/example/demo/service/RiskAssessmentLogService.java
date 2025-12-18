package com.example.demo.service;

import com.example.demo.entity.RiskAssessmentLog;
import java.util.List;

public interface RiskAssessmentLogService {
    void logAssessment(RiskAssessmentLog log);
    List<RiskAssessmentLog> getLogsByRequest(Long requestId);
}
