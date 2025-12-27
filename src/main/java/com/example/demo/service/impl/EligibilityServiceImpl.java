package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.service.EligibilityService;
import org.springframework.stereotype.Service;

@Service   // ✅ ADD THIS
public class EligibilityServiceImpl implements EligibilityService {

    private final LoanRequestRepository loanRequestRepository;
    private final FinancialProfileRepository profileRepository;
    private final EligibilityResultRepository resultRepository;

    public EligibilityServiceImpl(
            LoanRequestRepository loanRequestRepository,
            FinancialProfileRepository profileRepository,
            EligibilityResultRepository resultRepository) {
        this.loanRequestRepository = loanRequestRepository;
        this.profileRepository = profileRepository;
        this.resultRepository = resultRepository;
    }

    @Override
    public EligibilityResult evaluateEligibility(Long loanRequestId) {

        if (resultRepository.findByLoanRequestId(loanRequestId).isPresent()) {
            throw new BadRequestException("Financial profile already exists");
        }

        LoanRequest loanRequest = loanRequestRepository.findById(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        FinancialProfile profile = profileRepository
                .findByUserId(loanRequest.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        EligibilityResult result = new EligibilityResult();
        result.setLoanRequest(loanRequest);
        result.setIsEligible(profile.getCreditScore() >= 600);
        result.setMaxEligibleAmount(profile.getMonthlyIncome() * 10);
        result.setEstimatedEmi(1000.0);
        result.setRiskLevel("LOW");

        return resultRepository.save(result);
    }

    @Override
    public EligibilityResult getByLoanRequestId(Long loanRequestId) {
        return resultRepository.findByLoanRequestId(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
