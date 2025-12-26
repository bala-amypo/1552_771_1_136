package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.*;
import com.example.demo.repository.*;

public class FinancialProfileServiceImpl {

    private final FinancialProfileRepository repo;
    private final UserRepository userRepo;

    public FinancialProfileServiceImpl(FinancialProfileRepository repo, UserRepository userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
    }

    public FinancialProfile createOrUpdate(FinancialProfile fp) {
        if (fp.getCreditScore() < 300 || fp.getCreditScore() > 900)
            throw new BadRequestException("creditScore");

        Long userId = fp.getUser().getId();
        FinancialProfile existing = repo.findByUserId(userId).orElse(null);

        if (existing != null) {
            fp.setId(existing.getId());
        }
        return repo.save(fp);
    }

    public FinancialProfile getByUserId(Long userId) {
        return repo.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Financial profile not found"));
    }
}
