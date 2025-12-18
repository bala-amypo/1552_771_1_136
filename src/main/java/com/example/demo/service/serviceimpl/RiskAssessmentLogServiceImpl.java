package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.RiskAssessmentLogService;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;

public class RiskAssessmentLogServiceImpl implements RiskAssessmentService {

    private final LoanRequestRepository loanRepository;
    private final FinancialProfileRepository profileRepository;
    private final RiskAssessmentLogRepository logRepository;

    public RiskAssessmentServiceImpl(LoanRequestRepository loanRepository,
                                     FinancialProfileRepository profileRepository,
                                     RiskAssessmentLogRepository logRepository) {
        this.loanRepository = loanRepository;
        this.profileRepository = profileRepository;
        this.logRepository = logRepository;
    }

    @Override
    public RiskAssessmentLog assessRisk(Long loanRequestId) {

        if (logRepository.findByLoanRequestId(loanRequestId).isPresent()) {
            throw new BadRequestException("Risk already assessed");
        }

        LoanRequest loan = loanRepository.findById(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found"));

        FinancialProfile profile = profileRepository.findByUserId(loan.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Financial profile not found"));

        double obligations = profile.getMonthlyExpenses() +
                (profile.getExistingLoanEmi() == null ? 0 : profile.getExistingLoanEmi());

        double dti = obligations / profile.getMonthlyIncome();

        RiskAssessmentLog log = new RiskAssessmentLog();
        log.setLoanRequestId(loanRequestId);
        log.setDtiRatio(dti);

        if (profile.getCreditScore() >= 700) {
            log.setCreditCheckStatus("APPROVED");
        } else if (profile.getCreditScore() >= 600) {
            log.setCreditCheckStatus("PENDING_REVIEW");
        } else {
            log.setCreditCheckStatus("REJECTED");
        }

        return logRepository.save(log);
    }

    @Override
    public RiskAssessmentLog getByLoanRequestId(Long loanRequestId) {
        return logRepository.findByLoanRequestId(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Risk assessment not found"));
    }
}
