package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.LoanEligibilityService;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoanEligibilityServiceImpl implements LoanEligibilityService {

    private final LoanRequestRepository loanRequestRepository;
    private final FinancialProfileRepository profileRepository;
    private final EligibilityResultRepository eligibilityResultRepository;

    public LoanEligibilityServiceImpl(LoanRequestRepository loanRequestRepository, 
                                     FinancialProfileRepository profileRepository, 
                                     EligibilityResultRepository eligibilityResultRepository) {
        this.loanRequestRepository = loanRequestRepository;
        this.profileRepository = profileRepository;
        this.eligibilityResultRepository = eligibilityResultRepository;
    }

    @Override
    @Transactional
    public EligibilityResult evaluateEligibility(Long loanRequestId) {
        if (eligibilityResultRepository.findByLoanRequestId(loanRequestId).isPresent()) {
            throw new BadRequestException("Eligibility already evaluated");
        }

        LoanRequest request = loanRequestRepository.findById(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found"));
        
        FinancialProfile profile = profileRepository.findByUserId(request.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Financial profile not found"));

        EligibilityResult result = new EligibilityResult();
        result.setLoanRequest(request);

        
        double dti = ((profile.getMonthlyExpenses() + profile.getExistingLoanEmi()) / profile.getMonthlyIncome()) * 100;
        int score = profile.getCreditScore();

        if (dti > 50 || score < 600) {
            result.setIsEligible(false);
            result.setRejectionReason(dti > 50 ? "DTI exceeds 50%" : "Credit score too low");
            result.setMaxEligibleAmount(0.0);
            result.setEstimatedEmi(0.0);
            result.setRiskLevel("HIGH");
            request.setStatus("REJECTED");
        } else {
            result.setIsEligible(true);
            result.setMaxEligibleAmount(profile.getMonthlyIncome() * 10);
            result.setEstimatedEmi(result.getMaxEligibleAmount() / request.getTenureMonths());
            result.setRiskLevel(score >= 750 ? "LOW" : "MEDIUM");
            request.setStatus("APPROVED");
        }

        loanRequestRepository.save(request);
        return eligibilityResultRepository.save(result);
    }

    @Override
    public EligibilityResult getByLoanRequestId(Long loanRequestId) {
        return eligibilityResultRepository.findByLoanRequestId(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Result not found"));
    }
}