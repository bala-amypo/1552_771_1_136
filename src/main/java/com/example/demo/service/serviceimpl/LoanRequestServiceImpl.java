package com.example.demo.service.impl;

import com.example.demo.entity.LoanRequest;
import com.example.demo.exception.*;
import com.example.demo.repository.LoanRequestRepository;
import com.example.demo.service.LoanRequestService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LoanRequestServiceImpl implements LoanRequestService {

    private final LoanRequestRepository repo;

    public LoanRequestServiceImpl(LoanRequestRepository repo) { this.repo = repo; }

    public LoanRequest submitLoanRequest(LoanRequest request) {
        if(request.getRequestedAmount() <= 0) throw new ValidationException("Invalid amount");
        if(request.getTenureMonths() <= 0) throw new ValidationException("Invalid tenure");
        return repo.save(request);
    }

    public List<LoanRequest> getRequestsByUser(Long userId) { return repo.findByUserId(userId); }
    public LoanRequest getRequestById(Long id) { return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Request not found")); }
    public List<LoanRequest> getAllRequests() { return repo.findAll(); }
}
