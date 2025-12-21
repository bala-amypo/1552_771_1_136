package com.example.demo.repository;

import com.example.demo.entity.LoanRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanRequestRepository extends JpaRepository<LoanRequest, Long> {
    // Retrieves all loan applications submitted by a specific user
    List<LoanRequest> findByUserId(Long userId);
    
    // Filters requests by their current status (PENDING, APPROVED, REJECTED)
    List<LoanRequest> findByStatus(String status);
}