package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.service.RiskAssessmentService;
import org.springframework.stereotype.Service;

@Service
public class RiskAssessmentServiceImpl implements RiskAssessmentService {
    private final RiskAssessmentLogRepository riskLogRepository;
    private final FinancialProfileRepository profileRepository;
    private final LoanRequestRepository loanRequestRepository;

    public RiskAssessmentServiceImpl(RiskAssessmentLogRepository riskLogRepository, 
                                     FinancialProfileRepository profileRepository,
                                     LoanRequestRepository loanRequestRepository) {
        this.riskLogRepository = riskLogRepository;
        this.profileRepository = profileRepository;
        this.loanRequestRepository = loanRequestRepository;
    }

    @Override
    public RiskAssessmentLog assessRisk(Long loanRequestId) {
        if (!riskLogRepository.findByLoanRequestId(loanRequestId).isEmpty()) {
            throw new BadRequestException("Risk already assessed");
        }

        LoanRequest request = loanRequestRepository.findById(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found"));

        FinancialProfile profile = profileRepository.findByUserId(request.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Financial profile not found"));

        double dti = (profile.getMonthlyExpenses() + (profile.getExistingLoanEmi() != null ? profile.getExistingLoanEmi() : 0)) / profile.getMonthlyIncome();

        RiskAssessmentLog log = new RiskAssessmentLog();
        log.setLoanRequestId(loanRequestId);
        log.setDtiRatio(dti);
        log.setCreditCheckStatus(profile.getCreditScore() > 600 ? "APPROVED" : "REJECTED");

        return riskLogRepository.save(log);
    }

    // This was the missing method causing the error
    @Override
    public RiskAssessmentLog getByLoanRequestId(Long loanRequestId) {
        return riskLogRepository.findByLoanRequestId(loanRequestId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Risk log not found"));
    }
}