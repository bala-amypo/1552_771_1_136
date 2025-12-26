package com.example.demo.repository;

import com.example.demo.entity.EligibilityResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EligibilityResultRepository extends JpaRepository<EligibilityResult, Long> {
    /**
     * Finds the eligibility result for a specific loan request.
     * Used to prevent duplicate evaluations[cite: 4, 132, 138].
     */
    Optional<EligibilityResult> findByLoanRequestId(Long loanRequestId);
}