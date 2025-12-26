package com.example.demo.service;

import com.example.demo.entity.FinancialProfile;

public interface FinancialProfileService {
    /**
     * Creates or updates a user's financial profile with credit score validation.
     */
    FinancialProfile createOrUpdate(FinancialProfile profile);

    /**
     * Retrieves the financial profile associated with a specific user.
     */
    FinancialProfile getByUserId(Long userId);
}