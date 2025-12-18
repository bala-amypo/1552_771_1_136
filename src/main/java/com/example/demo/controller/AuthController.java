package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.exception.BadRequestException;
import com.example.demo.service.impl.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserServiceImpl userService;
    private final BCryptPasswordEncoder passwordEncoder;

    // ✅ Constructor injection
    public AuthController(UserServiceImpl userService) {
        this.userService = userService;
        this.passwordEncoder = new BCryptPasswordEncoder(); // Manual instance
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new BadRequestException("Password cannot be empty");
        }
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
        User existingUser = userService.findByEmail(user.getEmail());

        if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "Login successful");
        response.put("email", existingUser.getEmail());

        return ResponseEntity.ok(response);
    }
}
