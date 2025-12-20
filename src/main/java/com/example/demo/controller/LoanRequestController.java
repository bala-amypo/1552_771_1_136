package com.example.demo.controller;

import com.example.demo.entity.LoanRequest;
import com.example.demo.service.LoanRequestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loan-requests")
public class LoanRequestController {

    private final LoanRequestService loanRequestService;

    public LoanRequestController(LoanRequestService loanRequestService) {
        this.loanRequestService = loanRequestService;
    }

    @PostMapping
    public LoanRequest submitLoanRequest(@RequestBody LoanRequest request) {
        return loanRequestService.submitRequest(request);
    }

    @GetMapping("/user/{userId}")
    public List<LoanRequest> getByUser(@PathVariable Long userId) {
        return loanRequestService.getRequestsByUser(userId);
    }

    @GetMapping("/{id}")
    public LoanRequest getById(@PathVariable Long id) {
        return loanRequestService.getById(id);
    }

    @GetMapping
    public List<LoanRequest> getAll() {
        return loanRequestService.getAllRequests();
    }
}
