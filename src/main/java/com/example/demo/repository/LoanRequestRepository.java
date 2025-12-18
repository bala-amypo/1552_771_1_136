package com.example.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.example.demo.entity.LoanRequest;


public interface LoanRequestRepository extends JpaRepository<LoanRequest, Long> {
List<LoanRequest> findByUserId(Long userId);
List<LoanRequest> findByStatus(String status);
}