package com.example.demo.service.serviceimpl;

import com.example.demo.entity.LoanRequest;
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
    public LoanRequest submitRequest(LoanRequest request) {
        return repository.save(request);
    }

    @Override
    public List<LoanRequest> getRequestsByUser(Long userId) {
        return repository.findByUserId(userId); // make sure repository has this method
    }

    @Override
    public LoanRequest getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("LoanRequest not found"));
    }

    @Override
    public List<LoanRequest> getAllRequests() {
        return repository.findAll();
    }
}
