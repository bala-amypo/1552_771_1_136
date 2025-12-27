package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.service.RiskAssessmentService;
import org.springframework.stereotype.Service;

@Service   // ✅ ADD THIS
public class RiskAssessmentServiceImpl implements RiskAssessmentService {

    private final LoanRequestRepository loanRequestRepository;
    private final FinancialProfileRepository profileRepository;
    private final RiskAssessmentRepository riskRepository;

    public RiskAssessmentServiceImpl(
            LoanRequestRepository loanRequestRepository,
            FinancialProfileRepository profileRepository,
            RiskAssessmentRepository riskRepository) {
        this.loanRequestRepository = loanRequestRepository;
        this.profileRepository = profileRepository;
        this.riskRepository = riskRepository;
    }

    @Override
    public RiskAssessment assessRisk(Long loanRequestId) {

        if (riskRepository.findByLoanRequestId(loanRequestId).isPresent()) {
            throw new BadRequestException("Financial profile already exists");
        }

        LoanRequest loanRequest = loanRequestRepository.findById(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        FinancialProfile profile = profileRepository
                .findByUserId(loanRequest.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        RiskAssessment risk = new RiskAssessment();
        risk.setLoanRequestId(loanRequestId);

        double income = profile.getMonthlyIncome() == 0 ? 1 : profile.getMonthlyIncome();
        double obligations = profile.getMonthlyExpenses() + profile.getExistingLoanEmi();

        risk.setDtiRatio(obligations / income);
        risk.setRiskScore(50.0);
        risk.setCreditCheckStatus("OK");

        return riskRepository.save(risk);
    }

    @Override
    public RiskAssessment getByLoanRequestId(Long loanRequestId) {
        return riskRepository.findByLoanRequestId(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
