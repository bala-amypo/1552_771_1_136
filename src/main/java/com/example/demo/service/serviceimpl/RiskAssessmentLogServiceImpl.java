package com.example.demo.service.impl;

import com.example.demo.entity.RiskAssessmentLog;
import com.example.demo.repository.RiskAssessmentLogRepository;
import com.example.demo.service.RiskAssessmentLogService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RiskAssessmentLogServiceImpl implements RiskAssessmentLogService {

    private final RiskAssessmentLogRepository logRepository;

    public RiskAssessmentLogServiceImpl(RiskAssessmentLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    public RiskAssessmentLog logAssessment(RiskAssessmentLog log) {
        return logRepository.save(log);
    }

    @Override
    public List<RiskAssessmentLog> getLogsByRequest(Long loanRequestId) {
        return logRepository.findByLoanRequestId(loanRequestId);
    }
}
