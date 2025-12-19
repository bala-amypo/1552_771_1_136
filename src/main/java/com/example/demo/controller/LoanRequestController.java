package com.example.demo.controller;

import com.example.demo.entity.LoanRequest;
import com.example.demo.service.LoanRequestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loan-requests")
@Tag(name = "loan-request-controller")
public class LoanRequestController {

    private final LoanRequestService service;

    public LoanRequestController(LoanRequestService service) {
        this.service = service;
    }

    @PostMapping
    public LoanRequest submit(@RequestBody LoanRequest request) {
        return service.submit(request);
    }

    @GetMapping
    public List<LoanRequest> getAll() {
        return service.getAll();
    }
}
