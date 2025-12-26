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

    @PostMapping("/")
    public ResponseEntity<LoanRequest> submitRequest(@RequestBody LoanRequest request) {
        LoanRequest result = loanRequestService.submitRequest(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LoanRequest>> getRequestsByUser(@PathVariable Long userId) {
        List<LoanRequest> requests = loanRequestService.getRequestsByUser(userId);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanRequest> getById(@PathVariable Long id) {
        LoanRequest request = loanRequestService.getById(id);
        return ResponseEntity.ok(request);
    }

    @GetMapping("/")
    public ResponseEntity<List<LoanRequest>> getAll() {
        List<LoanRequest> allRequests = loanRequestService.getAllRequests();
        return ResponseEntity.ok(allRequests);
    }
}
