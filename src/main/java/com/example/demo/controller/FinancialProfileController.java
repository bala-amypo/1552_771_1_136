package com.example.demo.controller;

import com.example.demo.entity.FinancialProfile;
import com.example.demo.exception.BadRequestException;
import com.example.demo.service.FinancialProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/financial-profiles")
public class FinancialProfileController {

    private final FinancialProfileService profileService;

    public FinancialProfileController(FinancialProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping("/")
    public ResponseEntity<FinancialProfile> createOrUpdate(@RequestBody FinancialProfile profile) {
        if (profile.getUser() == null || profile.getMonthlyIncome() == null) {
            throw new BadRequestException("User and monthly income are required");
        }
        return ResponseEntity.ok(profileService.createOrUpdate(profile));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<FinancialProfile> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(profileService.getByUserId(userId));
    }
}
