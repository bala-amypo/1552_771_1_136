package com.example.loan.controller;

import com.example.loan.entity.LoanRequest;
import com.example.loan.service.LoanRequestService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/loan-requests")
public class LoanRequestController {

    private final LoanRequestService service;

    public LoanRequestController(LoanRequestService service) { this.service = service; }

    @PostMapping
    public LoanRequest submit(@RequestBody LoanRequest request) { return service.submitLoanRequest(request); }

    @GetMapping("/user/{userId}")
    public List<LoanRequest> getUserRequests(@PathVariable Long userId) { return service.getRequestsByUser(userId); }

    @GetMapping("/{id}")
    public LoanRequest getById(@PathVariable Long id) { return service.getRequestById(id); }

    @GetMapping
    public List<LoanRequest> getAll() { return service.getAllRequests(); }
}
