package com.example.demo.service;

import com.example.demo.entity.LoanRequest;
import java.util.List;

public interface LoanRequestService {
    /**
     * Submits a new loan request with amount and tenure validation.
     */
    LoanRequest submitRequest(LoanRequest request);

    /**
     * Retrieves all loan requests submitted by a specific user.
     */
    List<LoanRequest> getRequestsByUser(Long userId);

    /**
     * Retrieves a specific loan request by its ID.
     */
    LoanRequest getById(Long id);

    /**
     * Retrieves all loan requests in the system for admin review.
     */
    List<LoanRequest> getAllRequests();
}