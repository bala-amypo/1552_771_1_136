package com.example.demo.controller;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        User user = userService.findByEmail(request.getEmail());
        // Password validation omitted for simplicity
        String token = jwtUtil.generateToken(Map.of("role", user.getRole()), user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token, user.getId(), user.getEmail(), user.getRole(), user.getFullName()));
    }
}
