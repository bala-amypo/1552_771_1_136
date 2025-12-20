package com.example.demo.service;

import com.example.demo.entity.FinancialProfile;

public interface FinancialProfileService {

    /**
     * Create or update financial profile
     * @param profile FinancialProfile
     * @return saved FinancialProfile
     */
    FinancialProfile createOrUpdate(FinancialProfile profile);

    /**
     * Get financial profile by user ID
     * @param userId user id
     * @return FinancialProfile
     */
    FinancialProfile getByUserId(Long userId);
}
