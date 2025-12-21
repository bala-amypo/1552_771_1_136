package com.example.demo.repository;

import com.example.demo.entity.RiskAssessmentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RiskAssessmentLogRepository extends JpaRepository<RiskAssessmentLog, Long> {
    // Retrieves all audit logs associated with a specific loan request
    List<RiskAssessmentLog> findByLoanRequestId(Long loanRequestId);
}