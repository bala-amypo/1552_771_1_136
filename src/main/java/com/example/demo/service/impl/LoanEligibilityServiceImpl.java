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
public class LoanEligibilityServiceImpl implements LoanEligibilityService {

    private final LoanRequestRepository loanRequestRepository;
    private final FinancialProfileRepository profileRepository;
    private final EligibilityResultRepository resultRepository;

    public LoanEligibilityServiceImpl(LoanRequestRepository loanRequestRepository,
                                      FinancialProfileRepository profileRepository,
                                      EligibilityResultRepository resultRepository) {
        this.loanRequestRepository = loanRequestRepository;
        this.profileRepository = profileRepository;
        this.resultRepository = resultRepository;
    }

    @Override
    public EligibilityResult evaluateEligibility(Long loanRequestId) {

        LoanRequest loanRequest = loanRequestRepository.findById(loanRequestId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Loan request not found"));

        if (resultRepository.findByLoanRequestId(loanRequestId).isPresent()) {
            throw new BadRequestException("Eligibility already evaluated");
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

        EligibilityResult result = new EligibilityResult();
        result.setLoanRequest(loanRequest);

        if (profile.getCreditScore() < 600 || dti > 0.6) {
            result.setIsEligible(false);
            result.setRiskLevel("HIGH");
            result.setRejectionReason("High risk profile");
            result.setMaxEligibleAmount(0.0);
            result.setEstimatedEmi(0.0);
        } else {
            result.setIsEligible(true);
            result.setRiskLevel(dti < 0.3 ? "LOW" : "MEDIUM");
            result.setMaxEligibleAmount(loanRequest.getRequestedAmount());
            result.setEstimatedEmi(
                    loanRequest.getRequestedAmount() / loanRequest.getTenureMonths()
            );
        }

        return resultRepository.save(result);
    }

    @Override
    public EligibilityResult getByLoanRequestId(Long loanRequestId) {
        return resultRepository.findByLoanRequestId(loanRequestId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Eligibility result not found"));
    }
}
