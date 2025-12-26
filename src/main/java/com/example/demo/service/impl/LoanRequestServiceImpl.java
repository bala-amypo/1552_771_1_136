package com.example.demo.service.impl;

import com.example.demo.entity.LoanRequest;
import com.example.demo.entity.User;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.LoanRequestRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.LoanRequestService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanRequestServiceImpl implements LoanRequestService {

    private final LoanRequestRepository lrRepo;
    private final UserRepository userRepo;

    public LoanRequestServiceImpl(LoanRequestRepository lrRepo, UserRepository userRepo) {
        this.lrRepo = lrRepo;
        this.userRepo = userRepo;
    }

    @Override
    public LoanRequest submitRequest(LoanRequest request) {
        User user = userRepo.findById(request.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.getRequestedAmount() == null || request.getRequestedAmount() <= 0) {
            throw new BadRequestException("Requested amount invalid");
        }
        if (request.getTenureMonths() == null || request.getTenureMonths() <= 0) {
            throw new BadRequestException("tenureMonths must be > 0");
        }

        request.setUser(user);
        request.setStatus(LoanRequest.Status.PENDING.name());
        return lrRepo.save(request);
    }

    @Override
    public List<LoanRequest> getRequestsByUser(Long userId) {
        return lrRepo.findByUserId(userId);
    }

    @Override
    public LoanRequest getById(Long id) {
        return lrRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found"));
    }

    @Override
    public List<LoanRequest> getAllRequests() {
        return lrRepo.findAll();
    }
}
