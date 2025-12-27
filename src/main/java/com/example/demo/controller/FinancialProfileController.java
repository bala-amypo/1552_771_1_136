package com.example.demo.controller;

import com.example.demo.entity.FinancialProfile;
import com.example.demo.service.FinancialProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/financial-profiles")
public class FinancialProfileController {

    private final FinancialProfileService service;

    public FinancialProfileController(FinancialProfileService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<FinancialProfile> createOrUpdate(
            @RequestBody FinancialProfile profile) {
        return ResponseEntity.ok(service.createOrUpdate(profile));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<FinancialProfile> getByUser(
            @PathVariable Long userId) {
        return ResponseEntity.ok(service.getByUserId(userId));
    }
}
