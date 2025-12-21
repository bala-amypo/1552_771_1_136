package com.example.demo.service;

import com.example.demo.entity.LoanRequest;
import java.util.List;

public interface LoanRequestService {
    LoanRequest submitRequest(LoanRequest request);
    List<LoanRequest> getRequestsByUser(Long userId);
    LoanRequest getById(Long id);
    List<LoanRequest> getAllRequests();
}