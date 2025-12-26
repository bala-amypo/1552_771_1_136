package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.*;
import com.example.demo.repository.*;
import java.util.List;

public class LoanRequestServiceImpl {

    private final LoanRequestRepository repo;
    private final UserRepository userRepo;

    public LoanRequestServiceImpl(LoanRequestRepository repo, UserRepository userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
    }

    public LoanRequest submitRequest(LoanRequest lr) {
        if (lr.getRequestedAmount() <= 0)
            throw new BadRequestException("Requested amount");

        lr.setStatus(LoanRequest.Status.PENDING.name());
        return repo.save(lr);
    }

    public LoanRequest getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found"));
    }

    public List<LoanRequest> getRequestsByUser(Long userId) {
        return repo.findByUserId(userId);
    }
}
