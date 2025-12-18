package com.example.demo.service;


import java.util.List;
import com.example.demo.entity.LoanRequest;


public interface LoanRequestService {
LoanRequest submitRequest(LoanRequest request);
List<LoanRequest> getRequestsByUser(Long userId);
LoanRequest getById(Long id);
List<LoanRequest> getAllRequests();
}