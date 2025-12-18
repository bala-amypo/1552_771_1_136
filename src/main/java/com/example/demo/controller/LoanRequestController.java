package com.example.demo.controller;

import com.example.demo.entity.LoanRequest;
import com.example.demo.service.impl.LoanRequestServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/loan-requests")
public class LoanRequestController {
    private final LoanRequestServiceImpl loanRequestService;
    public LoanRequestController(LoanRequestServiceImpl loanRequestService){
        this.loanRequestService=loanRequestService;
    }

    @PostMapping
    public ResponseEntity<LoanRequest> submitRequest(@RequestBody LoanRequest request){
        return ResponseEntity.ok(loanRequestService.submitLoanRequest(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LoanRequest>> getByUser(@PathVariable Long userId){
        return ResponseEntity.ok(loanRequestService.getRequestsByUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanRequest> getById(@PathVariable Long id){
        return ResponseEntity.ok(loanRequestService.getRequestById(id));
    }

    @GetMapping
    public ResponseEntity<List<LoanRequest>> getAll(){
        return ResponseEntity.ok(loanRequestService.getAllRequests());
    }
}
