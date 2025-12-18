package com.example.loan.repository;

import com.example.loan.entity.EligibilityResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EligibilityResultRepository extends JpaRepository<EligibilityResult, Long> {
    Optional<EligibilityResult> findByLoanRequestId(Long loanRequestId);
}
