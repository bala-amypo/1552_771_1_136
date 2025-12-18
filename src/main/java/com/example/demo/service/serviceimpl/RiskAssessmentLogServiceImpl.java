package com.example.demo.serviceimpl;

import org.springframework.stereotype.Service;

import com.example.demo.entity.FinancialProfile;
import com.example.demo.entity.LoanRequest;
import com.example.demo.entity.RiskAssessmentLog;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.FinancialProfileRepository;
import com.example.demo.repository.LoanRequestRepository;
import com.example.demo.repository.RiskAssessmentLogRepository;
import com.example.demo.service.RiskAssessmentLogService;

@Service
public class RiskAssessmentLogServiceImpl implements RiskAssessmentLogService {

    private final LoanRequestRepository loanRequestRepository;
    private final FinancialProfileRepository profileRepository;
    private final RiskAssessmentLogRepository logRepository;

    // ✅ Constructor name = class name
    public RiskAssessmentLogServiceImpl(
            LoanRequestRepository loanRequestRepository,
            FinancialProfileRepository profileRepository,
            RiskAssessmentLogRepository logRepository) {

        this.loanRequestRepository = loanRequestRepository;
        this.profileRepository = profileRepository;
        this.logRepository = logRepository;
    }

    @Override
    public RiskAssessmentLog assessRisk(Long loanRequestId) {

        if (!logRepository.findByLoanRequestId(loanRequestId).isEmpty()) {
            throw new BadRequestException("Risk already assessed");
        }

        LoanRequest loan = loanRequestRepository.findById(loanRequestId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Loan request not found"));

        FinancialProfile profile = profileRepository
                .findByUserId(loan.getUser().getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Financial profile not found"));

        double existingEmi = profile.getExistingLoanEmi() == null
                ? 0
                : profile.getExistingLoanEmi();

        double dti = (profile.getMonthlyExpenses() + existingEmi)
                / profile.getMonthlyIncome();

        RiskAssessmentLog log = new RiskAssessmentLog();
        log.setLoanRequestId(loanRequestId);
        log.setDtiRatio(dti);

        if (profile.getCreditScore() >= 750) {
            log.setCreditCheckStatus("APPROVED");
        } else if (profile.getCreditScore() >= 650) {
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
