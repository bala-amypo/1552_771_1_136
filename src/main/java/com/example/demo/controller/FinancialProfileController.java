package com.example.demo.controller;

import com.example.demo.entity.FinancialProfile;
import com.example.demo.service.impl.FinancialProfileServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/financial-profiles")
public class FinancialProfileController {
    private final FinancialProfileServiceImpl profileService;
    public FinancialProfileController(FinancialProfileServiceImpl profileService){
        this.profileService=profileService;
    }

    @PostMapping
    public ResponseEntity<FinancialProfile> createOrUpdate(@RequestBody FinancialProfile profile){
        return ResponseEntity.ok(profileService.createOrUpdateProfile(profile));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<FinancialProfile> getByUser(@PathVariable Long userId){
        return ResponseEntity.ok(profileService.getProfileByUser(userId));
    }
}
