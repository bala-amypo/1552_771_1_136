package com.example.demo.service;

import com.example.demo.entity.LoanRequest;
import java.util.List;

public interface LoanRequestService {

    LoanRequest submitRequest(LoanRequest request);

    LoanRequest getById(Long id);

    List<LoanRequest> getRequestsByUser(Long userId);

    List<LoanRequest> getAll();
}
