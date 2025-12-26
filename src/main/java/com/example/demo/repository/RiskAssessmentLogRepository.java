package com.example.demo.repository;

import com.example.demo.entity.RiskAssessmentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RiskAssessmentLogRepository extends JpaRepository<RiskAssessmentLog, Long> {

    List<RiskAssessmentLog> findByLoanRequestId(Long loanRequestId);

    Optional<RiskAssessmentLog> findById(Long id);

    List<RiskAssessmentLog> findAll();
}
