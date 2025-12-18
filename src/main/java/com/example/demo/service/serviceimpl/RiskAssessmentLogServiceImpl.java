package com.example.loan.service.impl;

import com.example.loan.entity.RiskAssessmentLog;
import com.example.loan.repository.RiskAssessmentLogRepository;
import com.example.loan.service.RiskAssessmentLogService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RiskAssessmentLogServiceImpl implements RiskAssessmentLogService {

    private final RiskAssessmentLogRepository repo;

    public RiskAssessmentLogServiceImpl(RiskAssessmentLogRepository repo) { this.repo = repo; }

    public void logAssessment(RiskAssessmentLog log) { repo.save(log); }
    public List<RiskAssessmentLog> getLogsByRequest(Long requestId) { return repo.findByLoanRequestId(requestId); }
}
