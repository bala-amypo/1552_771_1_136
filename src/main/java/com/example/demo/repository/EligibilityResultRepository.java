package com.example.demo.repository;

import com.example.demo.model.EligibilityResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EligibilityResultRepository extends JpaRepository<EligibilityResult, Long> {
    // Standard CRUD operations
}