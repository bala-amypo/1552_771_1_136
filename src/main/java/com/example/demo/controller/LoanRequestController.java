package com.example.demo.controller;

import com.example.demo.dto.LoanDtos;
import com.example.demo.entity.LoanRequest;
import com.example.demo.service.LoanRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/loan-requests")
public class LoanRequestController {

    private final LoanRequestService loanRequestService;

    public LoanRequestController(LoanRequestService loanRequestService) {
        this.loanRequestService = loanRequestService;
    }

    @PostMapping
    public ResponseEntity<LoanDtos.LoanRequestDto> submitRequest(@RequestBody LoanRequest request) {
        LoanRequest saved = loanRequestService.submitRequest(request);
        LoanDtos.LoanRequestDto dto = mapToDto(saved);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanDtos.LoanRequestDto> getRequest(@PathVariable Long id) {
        LoanRequest request = loanRequestService.getById(id);
        return ResponseEntity.ok(mapToDto(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LoanDtos.LoanRequestDto>> getRequestsByUser(@PathVariable Long userId) {
        List<LoanRequest> requests = loanRequestService.getRequestsByUser(userId);
        return ResponseEntity.ok(requests.stream().map(this::mapToDto).collect(Collectors.toList()));
    }

    @GetMapping
    public ResponseEntity<List<LoanDtos.LoanRequestDto>> getAllRequests() {
        List<LoanRequest> requests = loanRequestService.getAllRequests();
        return ResponseEntity.ok(requests.stream().map(this::mapToDto).collect(Collectors.toList()));
    }

    private LoanDtos.LoanRequestDto mapToDto(LoanRequest request) {
        LoanDtos.LoanRequestDto dto = new LoanDtos.LoanRequestDto();
        dto.setId(request.getId());
        dto.setUserId(request.getUser().getId());
        dto.setRequestedAmount(request.getRequestedAmount());
        dto.setTenureMonths(request.getTenureMonths());
        dto.setPurpose(request.getPurpose());
        dto.setStatus(request.getStatus());
        dto.setAppliedAt(request.getAppliedAt());
        return dto;
    }
}
