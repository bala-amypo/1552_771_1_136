package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.RiskAssessmentService;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class RiskAssessmentServiceImpl implements RiskAssessmentService {

    private final LoanRequestRepository loanRequestRepository;
    private final FinancialProfileRepository profileRepository;
    private final RiskAssessmentLogRepository riskRepository;

    public RiskAssessmentServiceImpl(LoanRequestRepository loanRequestRepository, 
                                     FinancialProfileRepository profileRepository, 
                                     RiskAssessmentLogRepository riskRepository) {
        this.loanRequestRepository = loanRequestRepository;
        this.profileRepository = profileRepository;
        this.riskRepository = riskRepository;
    }

    @Override
    @Transactional
    public RiskAssessmentLog assessRisk(Long loanRequestId) {
        // Rule: Throw exception if log already exists
        if (!riskRepository.findByLoanRequestId(loanRequestId).isEmpty()) {
            throw new BadRequestException("Risk already assessed for this request");
        }

        LoanRequest request = loanRequestRepository.findById(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Request not found"));
        
        FinancialProfile profile = profileRepository.findByUserId(request.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Financial Profile not found"));

        // Calculation: Total Monthly Obligations / Monthly Income
        double totalObligations = profile.getMonthlyExpenses() + profile.getExistingLoanEmi();
        double dtiRatio = (totalObligations / profile.getMonthlyIncome()) * 100;

        // Perform "Credit Check" status assignment
        String status = (dtiRatio > 50 || profile.getCreditScore() < 600) ? "REJECTED" : "APPROVED";
        if (dtiRatio > 40 && dtiRatio <= 50) status = "PENDING_REVIEW";

        RiskAssessmentLog log = new RiskAssessmentLog(loanRequestId, dtiRatio, status);
        return riskRepository.save(log); // Persists to database
    }

    @Override
    public List<RiskAssessmentLog> getByLoanRequestId(Long loanRequestId) {
        return riskRepository.findByLoanRequestId(loanRequestId);
    }
}