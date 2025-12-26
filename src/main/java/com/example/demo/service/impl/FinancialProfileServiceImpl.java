package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.BadRequestException;
import com.example.demo.repository.*;

public class FinancialProfileServiceImpl {

    private final FinancialProfileRepository repo;
    private final UserRepository userRepo;

    public FinancialProfileServiceImpl(FinancialProfileRepository r, UserRepository u) {
        this.repo = r;
        this.userRepo = u;
    }

    public FinancialProfile createOrUpdate(FinancialProfile fp) {
        if (fp.getCreditScore() < 300 || fp.getCreditScore() > 900)
            throw new BadRequestException("Invalid credit score");

        Long uid = fp.getUser().getId();
        userRepo.findById(uid).orElseThrow();

        return repo.findByUserId(uid)
                .map(existing -> {
                    fp.setId(existing.getId());
                    return repo.save(fp);
                })
                .orElseGet(() -> repo.save(fp));
    }

    public FinancialProfile getByUserId(Long id) {
        return repo.findByUserId(id).orElse(null);
    }
}
