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
import org.springframework.stereotype.Service;

@Service
public class RiskAssessmentServiceLogImpl implements RiskAssessmentService {

    private final LoanRequestRepository loanRequestRepository;
    private final FinancialProfileRepository profileRepository;
    private final RiskAssessmentLogRepository riskRepository;

    public RiskAssessmentServiceImpl(
            LoanRequestRepository loanRequestRepository,
            FinancialProfileRepository profileRepository,
            RiskAssessmentLogRepository riskRepository) {
        this.loanRequestRepository = loanRequestRepository;
        this.profileRepository = profileRepository;
        this.riskRepository = riskRepository;
    }

    @Override
    public RiskAssessmentLog assessRisk(Long loanRequestId) {
        if(!riskRepository.findByLoanRequestId(loanRequestId).isEmpty()) {
            throw new BadRequestException("Risk already assessed");
        }

        LoanRequest request = loanRequestRepository.findById(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found"));

        FinancialProfile profile = profileRepository.findByUserId(request.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Financial profile not found"));

        double totalObligations = profile.getMonthlyExpenses() + (profile.getExistingLoanEmi() != null ? profile.getExistingLoanEmi() : 0);
        double dti = totalObligations / profile.getMonthlyIncome();

        String status = dti < 0.5 && profile.getCreditScore() >= 600 ? "APPROVED" : "REJECTED";

        RiskAssessmentLog log = new RiskAssessmentLog();
        log.setLoanRequestId(loanRequestId);
        log.setDtiRatio(dti);
        log.setCreditCheckStatus(status);

        return riskRepository.save(log);
    }

    @Override
    public RiskAssessmentLog getByLoanRequestId(Long loanRequestId) {
        return riskRepository.findByLoanRequestId(loanRequestId).stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Risk assessment log not found"));
    }
}
