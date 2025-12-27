package com.example.demo.service;

import com.example.demo.entity.FinancialProfile;
import java.util.Optional;

public interface FinancialProfileService {
    FinancialProfile createOrUpdate(FinancialProfile profile);
    Optional<FinancialProfile> getByUserId(Long userId); // Added to fix Controller error
}