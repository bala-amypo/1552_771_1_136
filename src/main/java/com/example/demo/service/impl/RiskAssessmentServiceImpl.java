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

    private final LoanRequestRepository lrRepo;
    private final FinancialProfileRepository fpRepo;
    private final RiskAssessmentLogRepository raRepo;

    public RiskAssessmentServiceImpl(LoanRequestRepository lrRepo,
                                     FinancialProfileRepository fpRepo,
                                     RiskAssessmentLogRepository raRepo) {
        this.lrRepo = lrRepo;
        this.fpRepo = fpRepo;
        this.raRepo = raRepo;
    }

    @Override
    public RiskAssessmentLog assessRisk(Long loanRequestId) {
        if (!lrRepo.findById(loanRequestId).isPresent()) {
            throw new ResourceNotFoundException("Loan request not found");
        }
        if (raRepo.findByLoanRequestId(loanRequestId).size() > 0) {
            throw new BadRequestException("Risk already assessed");
        }

        LoanRequest lr = lrRepo.findById(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found"));
        FinancialProfile fp = fpRepo.findByUserId(lr.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Financial profile not found"));

        double obligations = fp.getMonthlyExpenses() + fp.getExistingLoanEmi();
        double dti = obligations / fp.getMonthlyIncome();

        String creditCheckStatus = fp.getCreditScore() >= 700 ? "APPROVED"
                : (fp.getCreditScore() >= 600 ? "PENDING_REVIEW" : "REJECTED");

        RiskAssessmentLog log = new RiskAssessmentLog();
        log.setLoanRequestId(loanRequestId);
        log.setDtiRatio(dti);
        log.setCreditCheckStatus(creditCheckStatus);

        return raRepo.save(log);
    }

    @Override
    public RiskAssessmentLog getByLoanRequestId(Long loanRequestId) {
        return raRepo.findByLoanRequestId(loanRequestId).stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Risk assessment not found"));
    }
}
