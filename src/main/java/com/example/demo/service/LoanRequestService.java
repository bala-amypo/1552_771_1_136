package com.example.demo.service;

import com.example.demo.entity.LoanRequest;

import java.util.List;

public interface LoanRequestService {

    /**
     * Submit a loan request
     * @param request LoanRequest
     * @return saved LoanRequest
     */
    LoanRequest submitRequest(LoanRequest request);

    /**
     * Get loan requests for a user
     * @param userId user id
     * @return list of LoanRequest
     */
    List<LoanRequest> getRequestsByUser(Long userId);

    /**
     * Get loan request by ID
     * @param id loan request id
     * @return LoanRequest
     */
    LoanRequest getById(Long id);

    /**
     * Get all loan requests
     * @return list of LoanRequest
     */
    List<LoanRequest> getAllRequests();
}
