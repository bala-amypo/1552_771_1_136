package com.example.demo.service.impl;

import com.example.demo.model.EligibilityResult;
import com.example.demo.model.FinancialProfile;
import com.example.demo.model.LoanRequest;
import com.example.demo.repository.EligibilityResultRepository;
import com.example.demo.repository.FinancialProfileRepository;
import com.example.demo.repository.LoanRequestRepository;
import com.example.demo.service.LoanEligibilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class LoanEligibilityServiceImpl implements LoanEligibilityService {

    @Autowired
    private LoanRequestRepository loanRequestRepository;

    @Autowired
    private FinancialProfileRepository financialProfileRepository;

    @Autowired
    private EligibilityResultRepository eligibilityResultRepository;

    @Override
    @Transactional
    public EligibilityResult evaluateEligibility(Long loanRequestId) {
        LoanRequest loanRequest = loanRequestRepository.findById(loanRequestId)
                .orElseThrow(() -> new RuntimeException("Loan Request not found"));

        FinancialProfile profile = financialProfileRepository.findByUserId(loanRequest.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Financial Profile not found"));

        double requestedEmi = loanRequest.getRequestedAmount() / loanRequest.getTenureMonths();
        double dtiRatio = (profile.getExistingLoanEmi() + requestedEmi) / profile.getMonthlyIncome();

        EligibilityResult result = new EligibilityResult();
        result.setLoanRequest(loanRequest);
        result.setCalculatedAt(LocalDateTime.now());
        result.setEstimatedEmi(requestedEmi);

        // This matches the "DTI too high" logic from your Swagger logs
        if (dtiRatio > 0.4) {
            result.setIsEligible(false);
            result.setRejectionReason("DTI too high");
            result.setRiskLevel("HIGH");
            loanRequest.setStatus("REJECTED");
        } else {
            result.setIsEligible(true);
            result.setRiskLevel("LOW");
            loanRequest.setStatus("APPROVED");
        }

        loanRequestRepository.save(loanRequest);
        return eligibilityResultRepository.save(result);
    }

    @Override
    public EligibilityResult getByLoanRequestId(Long loanRequestId) {
        // Implementation for the missing method error
        return eligibilityResultRepository.findByLoanRequestId(loanRequestId);
    }
}