package com.example.demo.controller;

import com.example.demo.entity.FinancialProfile;
import com.example.demo.service.FinancialProfileService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/financial-profiles")
public class FinancialProfileController {
package com.example.demo.controller;

import com.example.demo.dto.LoanDtos;
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
    public ResponseEntity<LoanDtos.FinancialProfileDto> createOrUpdate(@RequestBody FinancialProfile profile) {
        FinancialProfile saved = profileService.createOrUpdate(profile);
        LoanDtos.FinancialProfileDto dto = new LoanDtos.FinancialProfileDto();
        dto.setId(saved.getId());
        dto.setUserId(saved.getUser().getId());
        dto.setMonthlyIncome(saved.getMonthlyIncome());
        dto.setMonthlyExpenses(saved.getMonthlyExpenses());
        dto.setExistingLoanEmi(saved.getExistingLoanEmi());
        dto.setCreditScore(saved.getCreditScore());
        dto.setSavingsBalance(saved.getSavingsBalance());
        dto.setLastUpdatedAt(saved.getLastUpdatedAt());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<LoanDtos.FinancialProfileDto> getProfile(@PathVariable Long userId) {
        FinancialProfile profile = profileService.getByUserId(userId);
        LoanDtos.FinancialProfileDto dto = new LoanDtos.FinancialProfileDto();
        dto.setId(profile.getId());
        dto.setUserId(profile.getUser().getId());
        dto.setMonthlyIncome(profile.getMonthlyIncome());
        dto.setMonthlyExpenses(profile.getMonthlyExpenses());
        dto.setExistingLoanEmi(profile.getExistingLoanEmi());
        dto.setCreditScore(profile.getCreditScore());
        dto.setSavingsBalance(profile.getSavingsBalance());
        dto.setLastUpdatedAt(profile.getLastUpdatedAt());
        return ResponseEntity.ok(dto);
    }
}

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
