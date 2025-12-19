package com.example.demo.service.serviceimpl;

import com.example.demo.entity.RiskAssessmentLog;
import com.example.demo.repository.RiskAssessmentLogRepository;
import com.example.demo.service.RiskAssessmentLogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RiskAssessmentLogServiceImpl implements RiskAssessmentLogService {

    @Autowired
    private RiskAssessmentLogRepository riskAssessmentLogRepository;

    @Override
    public RiskAssessmentLog logAssessment(RiskAssessmentLog log) {
        return riskAssessmentLogRepository.save(log);
    }

    @Override
    public List<RiskAssessmentLog> getLogsByRequest(Long loanRequestId) {
        return riskAssessmentLogRepository.findByLoanRequestId(loanRequestId);
    }
}
