package com.example.demo.repository;

import com.example.demo.entity.RiskAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiskAssessmentRepository extends JpaRepository<RiskAssessment, Long> {
    /**
     * Retrieves the risk assessment log associated with a specific loan request[cite: 4, 127].
     */
    Optional<RiskAssessment> findByLoanRequestId(Long loanRequestId);
}