package com.example.demo.service;

import com.example.demo.entity.LoanRequest;
import java.util.List;

public interface LoanRequestService {
    LoanRequest submit(LoanRequest request);
    List<LoanRequest> getByUser(Long userId);
    List<LoanRequest> getAll();
}
