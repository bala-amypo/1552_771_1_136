package com.example.demo.service;

import com.example.demo.entity.FinancialProfile;

public interface FinancialProfileService {
    FinancialProfile createOrUpdateProfile(FinancialProfile profile);
    FinancialProfile getProfileByUser(Long userId);
}
