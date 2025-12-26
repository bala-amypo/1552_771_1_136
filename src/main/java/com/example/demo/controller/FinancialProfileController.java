package com.example.demo.controller;

import com.example.demo.dto.FinancialProfileDto;
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

    // POST /api/financial-profiles
    @PostMapping
    public ResponseEntity<FinancialProfileDto> createOrUpdate(
            @RequestBody FinancialProfileDto dto) {
        return ResponseEntity.ok(service.createOrUpdate(dto));
    }

    // GET /api/financial-profiles/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<FinancialProfileDto> getByUserId(
            @PathVariable Long userId) {
        return ResponseEntity.ok(service.getByUserId(userId));
    }
}
