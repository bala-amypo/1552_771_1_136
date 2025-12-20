package com.example.demo.controller;

import com.example.demo.entity.LoanRequest;
import com.example.demo.service.LoanRequestService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<LoanRequest> submitRequest(@RequestBody LoanRequest request) {
        return ResponseEntity.ok(loanRequestService.submitRequest(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanRequest> getRequest(@PathVariable Long id) {
        return ResponseEntity.ok(loanRequestService.getById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LoanRequest>> getRequestsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(loanRequestService.getRequestsByUser(userId));
    }

    @GetMapping
    public ResponseEntity<List<LoanRequest>> getAllRequests() {
        return ResponseEntity.ok(loanRequestService.getAllRequests());
    }
}
