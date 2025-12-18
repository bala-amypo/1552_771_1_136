package com.example.demo.service;
import com.example.demo.entity.LoanRequest;
import java.util.List;

public interface LoanRequestService {
    LoanRequest submitLoanRequest(LoanRequest request);
    LoanRequest getRequestById(Long id);
    List<LoanRequest> getRequestsByUser(Long userId);
    List<LoanRequest> getAllRequests();
}
