package com.example.demo.repository;

import com.example.demo.entity.RiskAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiskAssessmentRepository
        extends JpaRepository<RiskAssessment, Long> {

    Optional<RiskAssessment> findByLoanRequestId(Long loanRequestId);
}
