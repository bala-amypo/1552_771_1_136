// src/main/java/com/example/demo/repository/RiskAssessmentRepository.java
package com.example.demo.repository;

import com.example.demo.entity.RiskAssessment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RiskAssessmentRepository extends JpaRepository<RiskAssessment, Long> {
    Optional<RiskAssessment> findByLoanRequestId(Long loanRequestId);
    List<RiskAssessment> findAll();
}
