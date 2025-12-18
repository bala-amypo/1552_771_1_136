package com.example.loan.service;

import com.example.loan.entity.FinancialProfile;

public interface FinancialProfileService {
    FinancialProfile createOrUpdateProfile(FinancialProfile profile);
    FinancialProfile getProfileByUser(Long userId);
}
