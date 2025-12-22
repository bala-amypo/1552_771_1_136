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

    // Constructor Injection
    public LoanRequestController(LoanRequestService loanRequestService) {
        this.loanRequestService = loanRequestService;
    }

    @PostMapping("/") // Submits new loan request
    public ResponseEntity<LoanRequest> submit(@RequestBody LoanRequest request) {
        return ResponseEntity.ok(loanRequestService.submitRequest(request));
    }

    @GetMapping("/user/{userId}") // Gets requests for user
    public ResponseEntity<List<LoanRequest>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(loanRequestService.getRequestsByUser(userId));
    }

    @GetMapping("/{id}") // Retrieves specific request
    public ResponseEntity<LoanRequest> getById(@PathVariable Long id) {
        return ResponseEntity.ok(loanRequestService.getById(id));
    }

    @GetMapping("/") // Lists all requests
    public ResponseEntity<List<LoanRequest>> getAll() {
        return ResponseEntity.ok(loanRequestService.getAllRequests());
    }
}
