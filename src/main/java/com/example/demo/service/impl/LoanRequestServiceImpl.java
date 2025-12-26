package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.BadRequestException;
import com.example.demo.repository.*;

import java.util.List;

public class LoanRequestServiceImpl {

    private final LoanRequestRepository repo;
    private final UserRepository userRepo;

    public LoanRequestServiceImpl(LoanRequestRepository r, UserRepository u) {
        this.repo = r;
        this.userRepo = u;
    }

    public LoanRequest submitRequest(LoanRequest lr) {
        if (lr.getRequestedAmount() <= 0)
            throw new BadRequestException("Invalid amount");

        userRepo.findById(lr.getUser().getId()).orElseThrow();
        return repo.save(lr);
    }

    public LoanRequest getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public List<LoanRequest> getRequestsByUser(Long id) {
        return repo.findByUserId(id);
    }
}
