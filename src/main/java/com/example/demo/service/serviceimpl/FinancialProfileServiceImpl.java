package com.example.demo.service.impl;

import com.example.demo.entity.FinancialProfile;
import com.example.demo.exception.*;
import com.example.demo.repository.FinancialProfileRepository;
import com.example.demo.service.FinancialProfileService;
import org.springframework.stereotype.Service;

@Service
public class FinancialProfileServiceImpl implements FinancialProfileService {

    private final FinancialProfileRepository repo;

    public FinancialProfileServiceImpl(FinancialProfileRepository repo) { this.repo = repo; }

    public FinancialProfile createOrUpdateProfile(FinancialProfile profile) {
        if(repo.findByUserId(profile.getUser().getId()).isPresent())
            throw new DuplicateRecordException("Financial profile already exists");
        return repo.save(profile);
    }

    public FinancialProfile getProfileByUser(Long userId) {
        return repo.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
    }
}
