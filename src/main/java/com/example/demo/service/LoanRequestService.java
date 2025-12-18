package com.example.demo.service;

import com.example.demo.entity.LoanRequest;
import java.util.List;

public interface LoanRequestService {
    LoanRequest submitRequest(LoanRequest request);          // for creating a loan request
    List<LoanRequest> getRequestsByUser(Long userId);       // get all requests for a user
    LoanRequest getById(Long id);                           // get request by id
    List<LoanRequest> getAllRequests();                     // get all requests
}
