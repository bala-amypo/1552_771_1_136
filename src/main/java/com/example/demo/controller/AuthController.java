package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotation.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name="User Profiles")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.register(user));
    }
    
    // Simplification for no-security: returns user details by email
    @GetMapping("/login/{email}")
    public ResponseEntity<User> login(@PathVariable String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }
}