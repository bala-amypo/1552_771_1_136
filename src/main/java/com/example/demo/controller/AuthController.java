package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        if (user.getEmail() == null || user.getPassword() == null || user.getFullName() == null) {
            throw new BadRequestException("Full name, email, and password are required");
        }
        return ResponseEntity.ok(userService.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        List<User> allUsers = userService.getAllUsers();
        for (User u : allUsers) {
            if (u.getEmail().equals(user.getEmail()) && u.getPassword().equals(user.getPassword())) {
                return ResponseEntity.ok("Login successful for user: " + u.getFullName());
            }
        }
        throw new BadRequestException("Invalid email or password");
    }
}
