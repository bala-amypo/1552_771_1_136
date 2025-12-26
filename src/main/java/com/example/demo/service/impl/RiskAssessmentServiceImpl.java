package com.example.demo.service.impl;

import com.example.demo.entity.FinancialProfile;
import com.example.demo.entity.LoanRequest;
import com.example.demo.entity.RiskAssessmentLog;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.FinancialProfileRepository;
import com.example.demo.repository.LoanRequestRepository;
import com.example.demo.repository.RiskAssessmentLogRepository;
import com.example.demo.service.RiskAssessmentService;

import java.time.LocalDateTime;
import java.util.List;

public class RiskAssessmentServiceImpl implements RiskAssessmentService {

    private final LoanRequestRepository loanRequestRepository;
    private final FinancialProfileRepository financialProfileRepository;
    private final RiskAssessmentLogRepository riskAssessmentLogRepository;

    public RiskAssessmentServiceImpl(LoanRequestRepository loanRequestRepository,
                                     FinancialProfileRepository financialProfileRepository,
                                     RiskAssessmentLogRepository riskAssessmentLogRepository) {
        this.loanRequestRepository = loanRequestRepository;
        this.financialProfileRepository = financialProfileRepository;
        this.riskAssessmentLogRepository = riskAssessmentLogRepository;
    }

    @Override
    public List<RiskAssessmentLog> getByLoanRequestId(Long loanRequestId) {
        return riskAssessmentLogRepository.findByLoanRequestId(loanRequestId);
    }

    @Override
    public RiskAssessmentLog save(RiskAssessmentLog log) {
        // Basic validation: one log per loan request
        List<RiskAssessmentLog> existing = riskAssessmentLogRepository.findByLoanRequestId(log.getLoanRequestId());
        if (!existing.isEmpty()) throw new BadRequestException("Risk already assessed");

        LoanRequest loanRequest = loanRequestRepository.findById(log.getLoanRequestId())
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found"));

        FinancialProfile profile = financialProfileRepository.findByUserId(loanRequest.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Financial profile not found"));

        double totalObligations = profile.getMonthlyExpenses() + 
                                  (profile.getExistingLoanEmi() != null ? profile.getExistingLoanEmi() : 0);
        log.setDtiRatio(totalObligations / profile.getMonthlyIncome());
        log.setTimestamp(LocalDateTime.now());

        return riskAssessmentLogRepository.save(log);
    }
}
