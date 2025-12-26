package com.example.demo.repository;

import com.example.demo.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface EligibilityResultRepository
        extends JpaRepository<EligibilityResult, Long> {

    Optional<EligibilityResult> findByLoanRequestId(Long loanRequestId);
}
