package com.example.demo.service.impl;

import com.example.demo.entity.LoanRequest;
import com.example.demo.entity.User;
import com.example.demo.repository.LoanRequestRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.LoanRequestService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanRequestServiceImpl implements LoanRequestService {

    private final LoanRequestRepository loanRequestRepository;
    private final UserRepository userRepository;

    public LoanRequestServiceImpl(LoanRequestRepository loanRequestRepository, UserRepository userRepository) {
        this.loanRequestRepository = loanRequestRepository;
        this.userRepository = userRepository;
    }

    @Override
    public LoanRequest submitRequest(LoanRequest request) {
        if(request.getRequestedAmount() <= 0) {
            throw new RuntimeException("Requested amount must be greater than 0");
        }
        if(request.getTenureMonths() <= 0) {
            throw new RuntimeException("Tenure months must be greater than 0");
        }

        User user = userRepository.findById(request.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        request.setUser(user);
        request.setAppliedAt(LocalDateTime.now());
        request.setStatus("PENDING");

        return loanRequestRepository.save(request);
    }

    @Override
    public LoanRequest getById(Long id) {
        return loanRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan request not found"));
    }

    @Override
    public List<LoanRequest> getRequestsByUser(Long userId) {
        return loanRequestRepository.findByUserId(userId);
    }

    @Override
    public List<LoanRequest> getAllRequests() {
        return loanRequestRepository.findAll();
    }
}
