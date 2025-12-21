package com.example.demo.service.impl;

import org.springframework.transaction.annotation.Transactional;
// ... other imports

@Service
public class LoanRequestServiceImpl implements LoanRequestService {

    private final LoanRequestRepository loanRequestRepository;
    private final UserRepository userRepository;

    public LoanRequestServiceImpl(LoanRequestRepository loanRequestRepository, UserRepository userRepository) {
        this.loanRequestRepository = loanRequestRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional // CRITICAL: This ensures the 'insert' is saved to the DB
    public LoanRequest submitRequest(LoanRequest request) {
        if (request.getRequestedAmount() <= 0) {
            throw new BadRequestException("Requested amount must be > 0");
        }
        
        // Fetch the user to ensure the link is valid
        User user = userRepository.findById(request.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        request.setUser(user);
        return loanRequestRepository.save(request);
    }
    // ... rest of the code
}