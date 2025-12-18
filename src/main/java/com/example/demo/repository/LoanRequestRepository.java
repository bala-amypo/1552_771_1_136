package com.example.demo.repository;

import com.example.demo.entity.LoanRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRequestRepository extends JpaRepository<LoanRequest, Long> { }
