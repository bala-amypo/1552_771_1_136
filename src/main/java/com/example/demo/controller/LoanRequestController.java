package com.example.demo.controller;

import com.example.demo.entity.LoanRequest;
import com.example.demo.service.LoanRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loan-requests")
@Tag(name = "Loan Requests", description = "Endpoints for managing loan requests")
public class LoanRequestController {

    private final LoanRequestService service;

    public LoanRequestController(LoanRequestService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Submit a new loan request")
    public LoanRequest submit(@RequestBody LoanRequest request) {
        return service.submitLoanRequest(request);
    }

    @GetMapping
    @Operation(summary = "Get all loan requests")
    public List<LoanRequest> getAll() {
        return service.getAllRequests();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get loan request by ID")
    public LoanRequest getById(@PathVariable Long id) {
        return service.getRequestById(id);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get loan requests by user ID")
    public List<LoanRequest> getByUser(@PathVariable Long userId) {
        return service.getRequestsByUser(userId);
    }
}
