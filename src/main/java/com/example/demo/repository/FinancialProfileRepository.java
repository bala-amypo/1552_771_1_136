package com.example.demo.repository;

import com.example.demo.entity.FinancialProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FinancialProfileRepository extends JpaRepository<FinancialProfile, Long> {
    // Retrieves the unique profile linked to a specific User ID
    Optional<FinancialProfile> findByUserId(Long userId);
}