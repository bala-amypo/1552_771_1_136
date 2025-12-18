package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import com.example.demo.entity.FinancialProfile;
import com.example.demo.service.FinancialProfileService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/financial-profile")
@RequiredArgsConstructor
public class FinancialProfileController {

    private final FinancialProfileService profileService;

    @PostMapping
    public FinancialProfile createProfile(@RequestBody FinancialProfile profile) {
        return profileService.createProfile(profile);
    }

    @GetMapping("/{userId}")
    public FinancialProfile getProfile(@PathVariable Long userId) {
        return profileService.getByUserId(userId);
    }

    @PutMapping
    public FinancialProfile updateProfile(@RequestBody FinancialProfile profile) {
        return profileService.updateProfile(profile);
    }
}
