package com.example.demo.repository;

import com.example.demo.entity.FinancialProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FinancialProfileRepository extends JpaRepository<FinancialProfile, Long> {
    /**
     * Finds the financial profile associated with a specific user ID.
     * Enforces the one-profile-per-user rule[cite: 4, 75].
     */
    Optional<FinancialProfile> findByUserId(Long userId);
}