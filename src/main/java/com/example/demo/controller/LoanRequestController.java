package com.example.demo.controller;

import com.example.demo.entity.LoanRequest;
import com.example.demo.exception.BadRequestException;
import com.example.demo.service.LoanRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loan-requests")
public class LoanRequestController {

    private final LoanRequestService loanService;

    public LoanRequestController(LoanRequestService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/")
    public ResponseEntity<LoanRequest> submitRequest(@RequestBody LoanRequest request) {
        if (request.getRequestedAmount() == null || request.getRequestedAmount() <= 0) {
            throw new BadRequestException("Requested amount must be > 0");
        }
        return ResponseEntity.ok(loanService.submitRequest(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LoanRequest>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(loanService.getRequestsByUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanRequest> getById(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.getById(id));
    }

    @GetMapping("/")
    public ResponseEntity<List<LoanRequest>> getAll() {
        return ResponseEntity.ok(loanService.getAllRequests());
    }
}
