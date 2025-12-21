package com.example.demo.repository;

import com.example.demo.entity.EligibilityResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EligibilityResultRepository extends JpaRepository<EligibilityResult, Long> {
    // Finds result for a specific loan request
    Optional<EligibilityResult> findByLoanRequestId(Long loanRequestId);
}