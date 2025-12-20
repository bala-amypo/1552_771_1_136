package com.example.demo.controller;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest request) {
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole("CUSTOMER");

        User savedUser = userService.register(user);

        AuthResponse response = new AuthResponse();
        response.setUserId(savedUser.getId());
        response.setEmail(savedUser.getEmail());
        response.setFullName(savedUser.getFullName());
        response.setRole(savedUser.getRole());
        // Token generation skipped if JWT not used
        response.setToken("dummy-token");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        User user = userService.findByEmail(request.getEmail());
        if (user == null || !user.getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(401).body(null);
        }

        AuthResponse response = new AuthResponse();
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        response.setRole(user.getRole());
        response.setToken("dummy-token"); // Token placeholder

        return ResponseEntity.ok(response);
    }
}
