package com.example.demo.controller;

import com.example.demo.entity.FinancialProfile;
import com.example.demo.service.FinancialProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/financial-profiles")
@Tag(name = "financial-profile-controller")
public class FinancialProfileController {

    private final FinancialProfileService service;

    public FinancialProfileController(FinancialProfileService service) {
        this.service = service;
    }

    @PostMapping
    public FinancialProfile createOrUpdate(@RequestBody FinancialProfile profile) {
        return service.createOrUpdate(profile);
    }

    @GetMapping("/user/{userId}")
    public FinancialProfile getByUser(@PathVariable Long userId) {
        return service.getByUserId(userId);
    }
}
