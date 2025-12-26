// src/main/java/com/example/demo/service/impl/LoanRequestServiceImpl.java
package com.example.demo.service.impl;

import com.example.demo.entity.LoanRequest;
import com.example.demo.entity.User;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.LoanRequestRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.LoanRequestService;

import java.util.List;
import java.util.Objects;

public class LoanRequestServiceImpl implements LoanRequestService {
    private final LoanRequestRepository lrRepo;
    private final UserRepository userRepo;

    public LoanRequestServiceImpl(LoanRequestRepository lrRepo, UserRepository userRepo) {
        this.lrRepo = lrRepo; this.userRepo = userRepo;
    }

    @Override
    public LoanRequest submitRequest(LoanRequest request) {
        Objects.requireNonNull(lrRepo, "LoanRequestRepository required");
        Objects.requireNonNull(userRepo, "UserRepository required");
        if (request.getUser() == null || request.getUser().getId() == null)
            throw new ResourceNotFoundException("User not found");
        User u = userRepo.findById(request.getUser().getId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (request.getRequestedAmount() == null || request.getRequestedAmount() <= 0.0)
            throw new BadRequestException("Requested amount invalid");
        if (request.getTenureMonths() == null || request.getTenureMonths() <= 0)
            throw new BadRequestException("tenureMonths must be > 0");

        request.setUser(u);
        request.setStatus(LoanRequest.Status.PENDING.name());
        return lrRepo.save(request);
    }

    @Override
    public List<LoanRequest> getRequestsByUser(Long userId) {
        Objects.requireNonNull(lrRepo, "LoanRequestRepository required");
        return lrRepo.findByUserId(userId);
    }

    @Override
    public LoanRequest getById(Long id) {
        Objects.requireNonNull(lrRepo, "LoanRequestRepository required");
        return lrRepo.findById(id).orElse(null);
    }

    @Override
    public List<LoanRequest> getAllRequests() {
        Objects.requireNonNull(lrRepo, "LoanRequestRepository required");
        return lrRepo.findAll();
    }
}
