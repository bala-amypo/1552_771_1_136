package com.example.demo.service.impl;

import com.example.demo.entity.LoanRequest;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.LoanRequestRepository;
import com.example.demo.service.LoanRequestService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LoanRequestServiceImpl implements LoanRequestService {

    private final LoanRequestRepository loanRequestRepository;

    public LoanRequestServiceImpl(LoanRequestRepository loanRequestRepository) {
        this.loanRequestRepository = loanRequestRepository;
    }

    @Override
    public LoanRequest submitLoanRequest(LoanRequest request) {
        if(request.getRequestedAmount() <=0) throw new BadRequestException("Requested amount must be positive");
        if(request.getTenureMonths() <=0) throw new BadRequestException("Tenure months must be positive");
        return loanRequestRepository.save(request);
    }

    @Override
    public LoanRequest getRequestById(Long id) {
        return loanRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found"));
    }

    @Override
    public List<LoanRequest> getRequestsByUser(Long userId) {
        return loanRequestRepository.findByUserId(userId);
    }

    @Override
    public List<LoanRequest> getAllRequests() {
        return loanRequestRepository.findAll();
    }
}
