// src/main/java/com/example/demo/controller/AuthController.java
package com.example.demo.controller;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.impl.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserServiceImpl userService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // Constructor signature matches test usage
    public AuthController(UserServiceImpl userService, JwtUtil jwtUtil, UserRepository userRepository) {
        this.userService = userService; this.jwtUtil = jwtUtil; this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) {
        User created = userService.register(user);
        Map<String,Object> claims = new HashMap<>();
        claims.put("email", created.getEmail());
        claims.put("role", created.getRole());
        claims.put("userId", created.getId());
        String token = jwtUtil.generateToken(claims, created.getEmail());
        return ResponseEntity.ok(new AuthResponse(token, created.getId(), created.getEmail(), created.getRole(), created.getFullName()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        Optional<User> uOpt = userRepository.findByEmail(req.getEmail());
        if (uOpt.isEmpty() || !encoder.matches(req.getPassword(), uOpt.get().getPassword())) {
            return ResponseEntity.status(401).build();
        }
        User u = uOpt.get();
        Map<String,Object> claims = new HashMap<>();
        claims.put("email", u.getEmail());
        claims.put("role", u.getRole());
        claims.put("userId", u.getId());
        String token = jwtUtil.generateToken(claims, u.getEmail());
        return ResponseEntity.ok(new AuthResponse(token, u.getId(), u.getEmail(), u.getRole(), u.getFullName()));
    }
}
