package com.example.demo.controller;

import com.example.demo.dto.LoanRequestDto;
import com.example.demo.service.LoanRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loan-requests")
public class LoanRequestController {

    private final LoanRequestService service;

    public LoanRequestController(LoanRequestService service) {
        this.service = service;
    }

    // POST /api/loan-requests
    @PostMapping
    public ResponseEntity<LoanRequestDto> submit(
            @RequestBody LoanRequestDto dto) {
        return new ResponseEntity<>(service.submit(dto), HttpStatus.CREATED);
    }

    // GET /api/loan-requests/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LoanRequestDto>> getByUser(
            @PathVariable Long userId) {
        return ResponseEntity.ok(service.getByUserId(userId));
    }

    // GET /api/loan-requests/{id}
    @GetMapping("/{id}")
    public ResponseEntity<LoanRequestDto> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // GET /api/loan-requests
    @GetMapping
    public ResponseEntity<List<LoanRequestDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
