package com.example.demo.controller;

import com.example.demo.entity.FinancialProfile;
import com.example.demo.service.FinancialProfileService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/financial-profiles")
public class FinancialProfileController {

    private final FinancialProfileService financialProfileService;

    public FinancialProfileController(FinancialProfileService financialProfileService) {
        this.financialProfileService = financialProfileService;
    }

    @PostMapping
    public FinancialProfile createOrUpdate(@RequestBody FinancialProfile profile) {
        return financialProfileService.createOrUpdate(profile);
    }

    @GetMapping("/user/{userId}")
    public FinancialProfile getByUserId(@PathVariable Long userId) {
        return financialProfileService.getByUserId(userId);
    }
}
