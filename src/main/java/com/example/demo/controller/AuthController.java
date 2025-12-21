package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.exception.BadRequestException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "User Registration and Authentication")
public class AuthController {

    private final UserService userService;

    // Constructor Injection
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a new user.
     * Prefix: /auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        // Business logic handles duplicate email validation
        return ResponseEntity.ok(userService.register(user));
    }

    /**
     * Authenticates user.
     * Prefix: /auth/login
     * Note: Per your request to exclude JWT, this returns the User entity on success.
     */
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User loginRequest) {
        // Fetch user by email
        User user = userService.findByEmail(loginRequest.getEmail());
        
        // Simple password check
        if (!user.getPassword().equals(loginRequest.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }
        
        return ResponseEntity.ok(user);
    }
}