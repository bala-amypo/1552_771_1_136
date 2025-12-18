package com.example.loan.service;

import com.example.loan.entity.LoanRequest;
import java.util.List;

public interface LoanRequestService {
    LoanRequest submitLoanRequest(LoanRequest request);
    List<LoanRequest> getRequestsByUser(Long userId);
    LoanRequest getRequestById(Long id);
    List<LoanRequest> getAllRequests();
}
