package com.example.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.example.demo.entity.FinancialProfile;


public interface FinancialProfileRepository extends JpaRepository<FinancialProfile, Long> {
Optional<FinancialProfile> findByUserId(Long userId);
}