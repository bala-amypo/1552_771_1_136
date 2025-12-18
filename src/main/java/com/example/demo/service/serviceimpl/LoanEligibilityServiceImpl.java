package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.service.LoanEligibilityService;
import org.springframework.stereotype.Service;

@Service
public class LoanEligibilityServiceImpl implements LoanEligibilityService {

    private final LoanRequestRepository loanRequestRepository;
    private final FinancialProfileRepository financialProfileRepository;
    private final EligibilityResultRepository eligibilityResultRepository;
    private final RiskAssessmentLogRepository riskLogRepository;

    public LoanEligibilityServiceImpl(LoanRequestRepository loanRequestRepository,
                                      FinancialProfileRepository financialProfileRepository,
                                      EligibilityResultRepository eligibilityResultRepository,
                                      RiskAssessmentLogRepository riskLogRepository) {
        this.loanRequestRepository = loanRequestRepository;
        this.financialProfileRepository = financialProfileRepository;
        this.eligibilityResultRepository = eligibilityResultRepository;
        this.riskLogRepository = riskLogRepository;
    }

    @Override
    public EligibilityResult evaluateEligibility(Long loanRequestId) {
        LoanRequest request = loanRequestRepository.findById(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found"));

        FinancialProfile profile = financialProfileRepository.findByUserId(request.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Financial profile not found"));

        double dti = profile.getExistingLoanEmi()/profile.getMonthlyIncome();
        boolean eligible;
        String risk;
        String reason=null;

        if(profile.getCreditScore()<400 || dti>0.5){
            eligible=false; risk="HIGH"; reason="Credit score too low or DTI too high";
        } else if(profile.getCreditScore()<600 || dti>0.35){
            eligible=true; risk="MEDIUM";
        } else {
            eligible=true; risk="LOW";
        }

        double maxAmount=eligible ? profile.getMonthlyIncome()*0.5*request.getTenureMonths() : 0;
        double estimatedEmi=eligible ? maxAmount/request.getTenureMonths() : 0;

        EligibilityResult result=new EligibilityResult();
        result.setLoanRequest(request);
        result.setIsEligible(eligible);
        result.setMaxEligibleAmount(maxAmount);
        result.setEstimatedEmi(estimatedEmi);
        result.setRiskLevel(risk);
        result.setRejectionReason(reason);

        eligibilityResultRepository.save(result);

        RiskAssessmentLog log=new RiskAssessmentLog();
        log.setLoanRequestId(request.getId());
        log.setDtiRatio(dti);
        log.setCreditCheckStatus(eligible ? "PASS" : "FAIL");
        riskLogRepository.save(log);

        return result;
    }

    @Override
    public EligibilityResult getResultByRequest(Long loanRequestId) {
        return eligibilityResultRepository.findByLoanRequestId(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Eligibility result not found"));
    }
}
