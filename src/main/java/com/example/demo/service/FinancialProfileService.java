package com.example.demo.service;

import com.example.demo.entity.FinancialProfile;

public interface FinancialProfileService {
    FinancialProfile createProfile(FinancialProfile profile);
    FinancialProfile getByUserId(Long userId);
    FinancialProfile updateProfile(FinancialProfile profile);
}
