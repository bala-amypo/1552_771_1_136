package com.example.demo.repository;

import com.example.demo.entity.RiskAssessmentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiskAssessmentLogRepository
        extends JpaRepository<RiskAssessment, Long> {

    Optional<RiskAssessment> findByLoanRequestId(Long loanRequestId);
}
