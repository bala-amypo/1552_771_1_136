package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.example.demo.entity.LoanRequest;
import com.example.demo.service.LoanRequestService;

@RestController
@RequestMapping("/api/loan-requests")
public class LoanRequestController {

    private final LoanRequestService loanRequestService;

    public LoanRequestController(LoanRequestService loanRequestService) {
        this.loanRequestService = loanRequestService;
    }

    @PostMapping
    public ResponseEntity<LoanRequest> submitRequest(
            @RequestBody LoanRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(loanRequestService.submitRequest(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LoanRequest>> getByUser(
            @PathVariable Long userId) {
        return ResponseEntity.ok(
                loanRequestService.getRequestsByUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanRequest> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                loanRequestService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<LoanRequest>> getAll() {
        return ResponseEntity.ok(
                loanRequestService.getAllRequests());
    }
}
