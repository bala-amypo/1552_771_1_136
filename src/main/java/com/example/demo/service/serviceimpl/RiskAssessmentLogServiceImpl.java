package com.example.demo.service.impl;

import com.example.demo.entity.RiskAssessmentLog;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.RiskAssessmentLogRepository;
import com.example.demo.service.RiskAssessmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RiskAssessmentServiceImpl implements RiskAssessmentService {

    private final RiskAssessmentLogRepository repository;

    public RiskAssessmentServiceImpl(RiskAssessmentLogRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<RiskAssessmentLog> getLogs(Long loanRequestId) {
        List<RiskAssessmentLog> logs = repository.findByLoanRequestId(loanRequestId);
        if (logs.isEmpty()) {
            throw new ResourceNotFoundException("Risk log not found");
        }
        return logs;
    }
}
