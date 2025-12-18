package com.example.loan.repository;

import com.example.loan.entity.RiskAssessmentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RiskAssessmentLogRepository extends JpaRepository<RiskAssessmentLog, Long> {
    List<RiskAssessmentLog> findByLoanRequestId(Long loanRequestId);
}
