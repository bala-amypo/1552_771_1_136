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

    private final LoanRequestRepository repository;

    public LoanRequestServiceImpl(LoanRequestRepository repository) {
        this.repository = repository;
    }

    @Override
    public LoanRequest submit(LoanRequest request) {

        if (request.getRequestedAmount() <= 0) {
            throw new BadRequestException("Requested amount");
        }

        if (request.getTenureMonths() <= 0) {
            throw new BadRequestException("Invalid tenure");
        }

        return repository.save(request);
    }

    @Override
    public List<LoanRequest> getByUser(Long userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public LoanRequest getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found"));
    }

    @Override
    public List<LoanRequest> getAll() {
        return repository.findAll();
    }
}
