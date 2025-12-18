package com.example.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.example.demo.entity.RiskAssessmentLog;


public interface RiskAssessmentLogRepository extends JpaRepository<RiskAssessmentLog, Long> {
Optional<RiskAssessmentLog> findByLoanRequestId(Long loanRequestId);
}