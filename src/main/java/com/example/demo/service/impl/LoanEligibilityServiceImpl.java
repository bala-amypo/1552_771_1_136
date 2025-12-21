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
        // Fetch data based on the ID passed in Swagger (/api/eligibility/evaluate/7)
        LoanRequest loanRequest = loanRequestRepository.findById(loanRequestId)
                .orElseThrow(() -> new RuntimeException("Loan Request not found"));

        FinancialProfile profile = financialProfileRepository.findByUserId(loanRequest.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Financial Profile not found"));

        // Calculation logic
        double monthlyIncome = profile.getMonthlyIncome();
        double existingEmi = profile.getExistingLoanEmi();
        double requestedEmi = loanRequest.getRequestedAmount() / loanRequest.getTenureMonths();
        
        double dtiRatio = (existingEmi + requestedEmi) / monthlyIncome;

        EligibilityResult result = new EligibilityResult();
        result.setLoanRequest(loanRequest);
        result.setCalculatedAt(LocalDateTime.now());
        result.setEstimatedEmi(requestedEmi);

        // Logic based on your specific output: "DTI too high" and Status: "REJECTED"
        if (dtiRatio > 0.45) { // Threshold that triggers the rejection seen in your screenshot
            result.setIsEligible(false);
            result.setMaxEligibleAmount(0.0);
            result.setRiskLevel("HIGH");
            result.setRejectionReason("DTI too high");
            loanRequest.setStatus("REJECTED");
        } else {
            result.setIsEligible(true);
            result.setMaxEligibleAmount(monthlyIncome * 10);
            result.setRiskLevel("LOW");
            loanRequest.setStatus("APPROVED");
        }

        // Save updates to DB (Triggers the Hibernate 'update' and 'insert' logs)
        loanRequestRepository.save(loanRequest);
        return eligibilityResultRepository.save(result);
    }
}