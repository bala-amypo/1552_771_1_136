package com.example.demo.controller;

import com.example.demo.entity.FinancialProfile;
import com.example.demo.service.FinancialProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/financial-profile")
public class FinancialProfileController {

    @Autowired
    private FinancialProfileService service;

    @GetMapping("/user/{userId}")
    public ResponseEntity<FinancialProfile> getByUserId(@PathVariable Long userId) {
        // Handle the Optional returned by the service
        return service.getByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FinancialProfile> createOrUpdate(@RequestBody FinancialProfile profile) {
        return ResponseEntity.ok(service.createOrUpdate(profile));
    }
}