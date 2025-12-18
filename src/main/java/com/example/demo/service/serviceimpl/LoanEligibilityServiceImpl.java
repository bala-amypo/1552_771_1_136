package com.example.demo.service.impl;

import com.example.demo.entity.EligibilityResult;
import com.example.demo.entity.LoanRequest;
import com.example.demo.repository.EligibilityResultRepository;
import com.example.demo.service.LoanEligibilityService;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class LoanEligibilityServiceImpl implements LoanEligibilityService {

    private final EligibilityResultRepository eligibilityResultRepository;

    public LoanEligibilityServiceImpl(EligibilityResultRepository eligibilityResultRepository) {
        this.eligibilityResultRepository = eligibilityResultRepository;
    }

    @Override
    public EligibilityResult calculateEligibility(LoanRequest request) {
        EligibilityResult result = new EligibilityResult();
        result.setLoanRequest(request);

        // Example calculation logic
        if(request.getRequestedAmount() < 50000) {
            result.setIsEligible(true);
            result.setMaxEligibleAmount(request.getRequestedAmount());
            result.setEstimatedEmi(request.getRequestedAmount() / request.getTenureMonths());
            result.setRiskLevel("Low");
        } else {
            result.setIsEligible(false);
            result.setRejectionReason("Amount too high");
            result.setRiskLevel("High");
        }

        result.setCalculatedAt(Instant.now());
        return eligibilityResultRepository.save(result);
    }
}
