package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for user registration and login")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public User register(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    @Operation(summary = "Login user (no JWT, simple validation)")
    public User login(@RequestBody User user) {
        User existing = userService.findByEmail(user.getEmail());
        if (existing == null || !existing.getPassword().equals(user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }
        return existing;
    }
}
