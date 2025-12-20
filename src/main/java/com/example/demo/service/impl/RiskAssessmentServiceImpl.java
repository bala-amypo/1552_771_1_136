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
                .orElseThrow(() ->
                        new ResourceNotFoundException("Loan request not found"));

        if (!logRepository.findByLoanRequestId(loanRequestId).isEmpty()) {
            throw new BadRequestException("Risk already assessed");
        }

        FinancialProfile profile = profileRepository
                .findByUserId(loanRequest.getUser().getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Financial profile not found"));

        double totalObligations =
                profile.getMonthlyExpenses()
                        + (profile.getExistingLoanEmi() != null
                        ? profile.getExistingLoanEmi() : 0);

        double dti = totalObligations / profile.getMonthlyIncome();

        RiskAssessmentLog log = new RiskAssessmentLog();
        log.setLoanRequestId(loanRequestId);
        log.setDtiRatio(dti);

        if (profile.getCreditScore() >= 650) {
            log.setCreditCheckStatus("APPROVED");
        } else if (profile.getCreditScore() >= 550) {
            log.setCreditCheckStatus("PENDING_REVIEW");
        } else {
            log.setCreditCheckStatus("REJECTED");
        }

        return logRepository.save(log);
    }

    @Override
    public RiskAssessmentLog getByLoanRequestId(Long loanRequestId) {
        return logRepository.findByLoanRequestId(loanRequestId)
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException("Risk assessment not found"));
    }
}
