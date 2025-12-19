package com.example.demo.service;

import com.example.demo.entity.RiskAssessmentLog;
import java.util.List;

public interface RiskAssessmentLogService {

    RiskAssessmentLog logAssessment(RiskAssessmentLog log);

    List<RiskAssessmentLog> getLogsByRequest(Long loanRequestId);
}
