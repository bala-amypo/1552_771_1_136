package com.example.demo.service;

import com.example.demo.entity.LoanRequest;
import java.util.List;

public interface LoanRequestService {
    LoanRequest createLoanRequest(LoanRequest request);
    List<LoanRequest> getAllLoanRequests();
    LoanRequest getLoanRequestById(Long id);
}
