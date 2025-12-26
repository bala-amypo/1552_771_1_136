package com.example.demo.repository;

import com.example.demo.entity.LoanRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRequestRepository extends JpaRepository<LoanRequest, Long> {
    /**
     * Retrieves all loan requests submitted by a specific user[cite: 4, 55].
     */
    List<LoanRequest> findByUserId(Long userId);

    /**
     * Retrieves all loan requests with a specific status (e.g., PENDING, APPROVED).
     */
    List<LoanRequest> findByStatus(String status);
}