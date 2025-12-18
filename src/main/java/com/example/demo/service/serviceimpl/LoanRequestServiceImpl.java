package com.example.demo.service.serviceimpl;

import com.example.demo.entity.LoanRequest;
import com.example.demo.repository.LoanRequestRepository;
import com.example.demo.service.LoanRequestService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service  // ← critical: this makes it a Spring bean
public class LoanRequestServiceImpl implements LoanRequestService {

    private final LoanRequestRepository repository;

    public LoanRequestServiceImpl(LoanRequestRepository repository) {
        this.repository = repository;
    }

    @Override
    public LoanRequest createLoanRequest(LoanRequest request) {
        return repository.save(request);
    }

    @Override
    public List<LoanRequest> getAllLoanRequests() {
        return repository.findAll();
    }

    @Override
    public LoanRequest getLoanRequestById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("LoanRequest not found"));
    }
}
