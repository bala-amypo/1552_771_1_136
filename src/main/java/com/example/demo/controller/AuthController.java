package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.exception.BadRequestException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "User Registration and Authentication")
public class AuthController {

    private final UserService userService;

    // Constructor Injection as required by project rules
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * POST /register – Registers a new user
     * Validates that the email is unique via the service layer.
     */
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.register(user));
    }

    /**
     * POST /login – Authenticates user
     * Returns the User object if credentials are valid.
     * Note: Per requirements, JWT is excluded, returning Entity directly.
     */
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User loginRequest) {
        // Fetch user by email; throws ResourceNotFoundException if missing
        User user = userService.findByEmail(loginRequest.getEmail());
        
        // Manual password verification
        if (!user.getPassword().equals(loginRequest.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }
        
        return ResponseEntity.ok(user);
    }

    /**
     * GET /{id} – Helper endpoint to retrieve user by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    /**
     * GET /users – Helper endpoint to list all users.
     */
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}