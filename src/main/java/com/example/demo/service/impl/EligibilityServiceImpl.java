package com.example.demo.service.impl;

import com.example.demo.entity.EligibilityResult;
import com.example.demo.entity.FinancialProfile;
import com.example.demo.entity.LoanRequest;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.EligibilityResultRepository;
import com.example.demo.repository.FinancialProfileRepository;
import com.example.demo.repository.LoanRequestRepository;
import com.example.demo.service.LoanEligibilityService;
import org.springframework.stereotype.Service;

@Service
public class EligibilityServiceImpl implements LoanEligibilityService {

    private final LoanRequestRepository lrRepo;
    private final FinancialProfileRepository fpRepo;
    private final EligibilityResultRepository erRepo;

    public EligibilityServiceImpl(LoanRequestRepository lrRepo,
                                  FinancialProfileRepository fpRepo,
                                  EligibilityResultRepository erRepo) {
        this.lrRepo = lrRepo;
        this.fpRepo = fpRepo;
        this.erRepo = erRepo;
    }

    @Override
    public EligibilityResult evaluateEligibility(Long loanRequestId) {
        if (erRepo.findByLoanRequestId(loanRequestId).isPresent()) {
            throw new BadRequestException("Eligibility already evaluated");
        }

        LoanRequest lr = lrRepo.findById(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found"));
        FinancialProfile fp = fpRepo.findByUserId(lr.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Financial profile not found"));

        double obligations = fp.getMonthlyExpenses() + fp.getExistingLoanEmi();
        double dti = obligations / fp.getMonthlyIncome();

        String riskLevel = dti < 0.25 ? "LOW" : dti < 0.45 ? "MEDIUM" : "HIGH";
        boolean eligible = fp.getCreditScore() >= 600 && !"HIGH".equals(riskLevel);

        EligibilityResult er = new EligibilityResult();
        er.setLoanRequest(lr);
        er.setIsEligible(eligible);
        er.setRiskLevel(riskLevel);
        er.setMaxEligibleAmount(fp.getMonthlyIncome() * 10);
        er.setEstimatedEmi(er.getMaxEligibleAmount() / lr.getTenureMonths());

        if (!eligible) {
            er.setRejectionReason("Eligibility failed");
        }

        return erRepo.save(er);
    }

    @Override
    public EligibilityResult getByLoanRequestId(Long loanRequestId) {
        return erRepo.findByLoanRequestId(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Eligibility result not found"));
    }
}
