package com.example.demo.service.impl;

import com.example.demo.entity.LoanRequest;
import com.example.demo.entity.User;
import com.example.demo.repository.LoanRequestRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.LoanRequestService;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class LoanRequestServiceImpl implements LoanRequestService {

    private final LoanRequestRepository loanRequestRepository;
    private final UserRepository userRepository;

    // Constructor injection as per architecture rules
    public LoanRequestServiceImpl(LoanRequestRepository loanRequestRepository, UserRepository userRepository) {
        this.loanRequestRepository = loanRequestRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public LoanRequest submitRequest(LoanRequest request) {
        // Validation logic
        if (request.getRequestedAmount() == null || request.getRequestedAmount() <= 0) {
            throw new BadRequestException("Requested amount must be > 0");
        }

        // Must fetch the existing user to avoid "No Content" or null user_id in DB
        User user = userRepository.findById(request.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        request.setUser(user); // Attaches the database user to the request
        return loanRequestRepository.save(request); // This saves to SQL
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