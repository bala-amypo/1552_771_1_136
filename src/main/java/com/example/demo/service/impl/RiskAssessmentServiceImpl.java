package com.example.demo.service.impl;

import com.example.demo.entity.FinancialProfile;
import com.example.demo.entity.LoanRequest;
import com.example.demo.entity.RiskAssessmentLog;
import com.example.demo.repository.FinancialProfileRepository;
import com.example.demo.repository.LoanRequestRepository;
import com.example.demo.repository.RiskAssessmentLogRepository;
import com.example.demo.service.RiskAssessmentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RiskAssessmentServiceImpl implements RiskAssessmentService {

    private final LoanRequestRepository loanRequestRepository;
    private final FinancialProfileRepository profileRepository;
    private final RiskAssessmentLogRepository logRepository;

    public RiskAssessmentServiceImpl(LoanRequestRepository loanRequestRepository,
                                     FinancialProfileRepository profileRepository,
                                     RiskAssessmentLogRepository logRepository) {
        this.loanRequestRepository = loanRequestRepository;
        this.profileRepository = profileRepository;
        this.logRepository = logRepository;
    }

    @Override
    public RiskAssessmentLog assessRisk(Long loanRequestId) {
        LoanRequest loanRequest = loanRequestRepository.findById(loanRequestId)
                .orElseThrow(() -> new RuntimeException("Loan request not found"));

        // Check if already assessed
        List<RiskAssessmentLog> existingLogs = logRepository.findByLoanRequestId(loanRequestId);
        if(!existingLogs.isEmpty()) {
            throw new RuntimeException("Risk already assessed");
        }

        FinancialProfile profile = profileRepository.findByUserId(loanRequest.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Financial profile not found"));

        double dti = (profile.getMonthlyExpenses() + 
                     (profile.getExistingLoanEmi() != null ? profile.getExistingLoanEmi() : 0)) / profile.getMonthlyIncome();

        String creditStatus = profile.getCreditScore() >= 600 ? "APPROVED" : "REJECTED";

        RiskAssessmentLog log = new RiskAssessmentLog();
        log.setLoanRequestId(loanRequestId);
        log.setDtiRatio(dti);
        log.setCreditCheckStatus(creditStatus);
        log.setTimestamp(LocalDateTime.now());

        return logRepository.save(log);
    }

    @Override
    public RiskAssessmentLog getByLoanRequestId(Long loanRequestId) {
        return logRepository.findByLoanRequestId(loanRequestId).stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Risk assessment log not found"));
    }
}
