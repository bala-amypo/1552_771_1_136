package com.example.demo.controller;

import com.example.demo.entity.FinancialProfile;
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

    @PostMapping
    public ResponseEntity<FinancialProfile> createOrUpdate(@RequestBody FinancialProfile profile) {
        return ResponseEntity.ok(profileService.createOrUpdate(profile));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<FinancialProfile> getProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(profileService.getByUserId(userId));
    }
}
