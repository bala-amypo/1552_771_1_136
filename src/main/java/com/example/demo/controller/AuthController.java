package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.impl.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

public class AuthController {

    private final UserServiceImpl userService;
    private final JwtUtil jwtUtil;
    private final UserRepository repo;

    public AuthController(UserServiceImpl u, JwtUtil j, UserRepository r) {
        this.userService = u;
        this.jwtUtil = j;
        this.repo = r;
    }

    public ResponseEntity<AuthResponse> login(AuthRequest req) {
        User u = repo.findByEmail(req.getEmail()).orElseThrow();
        if (!new BCryptPasswordEncoder().matches(req.getPassword(), u.getPassword()))
            throw new RuntimeException("Invalid");

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", u.getEmail());
        claims.put("role", u.getRole());
        claims.put("userId", u.getId());

        String token = jwtUtil.generateToken(claims, u.getEmail());
        return ResponseEntity.ok(new AuthResponse(token, u.getEmail()));
    }
}
