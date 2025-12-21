package com.example.demo.service.impl;

import com.example.demo.entity.LoanRequest;
import com.example.demo.entity.User;
import com.example.demo.repository.LoanRequestRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.LoanRequestService;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LoanRequestServiceImpl implements LoanRequestService {

    private final LoanRequestRepository loanRequestRepository;
    private final UserRepository userRepository;

    // Constructor Injection only
    public LoanRequestServiceImpl(LoanRequestRepository loanRequestRepository, UserRepository userRepository) {
        this.loanRequestRepository = loanRequestRepository;
        this.userRepository = userRepository;
    }

    @Override
    public LoanRequest submitRequest(LoanRequest request) {
        // Validation: requestedAmount must be > 0
        if (request.getRequestedAmount() == null || request.getRequestedAmount() <= 0) {
            throw new BadRequestException("Requested amount must be > 0");
        }
        
        // Validation: tenureMonths must be > 0
        if (request.getTenureMonths() == null || request.getTenureMonths() <= 0) {
            throw new BadRequestException("tenureMonths must be > 0");
        }

        // Ensure user exists before saving
        User user = userRepository.findById(request.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        request.setUser(user);
        return loanRequestRepository.save(request); // Persists to database
    }

    @Override
    public List<LoanRequest> getRequestsByUser(Long userId) {
        return loanRequestRepository.findByUserId(userId);
    }

    @Override
    public LoanRequest getById(Long id) {
        return loanRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found"));
    }

    @Override
    public List<LoanRequest> getAllRequests() {
        return loanRequestRepository.findAll();
    }
}