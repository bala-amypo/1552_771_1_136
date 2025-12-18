package com.example.demo.service.impl;

import com.example.demo.entity.LoanRequest;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.LoanRequestRepository;
import com.example.demo.service.LoanRequestService;

import java.util.List;

public class LoanRequestServiceImpl implements LoanRequestService {

    private final LoanRequestRepository requestRepository;

    // ✅ Constructor injection
    public LoanRequestServiceImpl(LoanRequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public LoanRequest submitLoanRequest(LoanRequest request) {
        if (request.getRequestedAmount() <= 0) {
            throw new BadRequestException("Requested amount must be positive");
        }
        if (request.getTenureMonths() <= 0) {
            throw new BadRequestException("Tenure months must be positive");
        }
        return requestRepository.save(request);
    }

    @Override
    public List<LoanRequest> getRequestsByUser(Long userId) {
        return requestRepository.findByUserId(userId);
    }

    @Override
    public LoanRequest getRequestById(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found"));
    }

    @Override
    public List<LoanRequest> getAllRequests() {
        return requestRepository.findAll();
    }
}
