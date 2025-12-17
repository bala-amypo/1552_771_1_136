package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {

        User saved = userService.register(user);

        if (saved == null) {
            return "Email already exists";
        }

        return "User registered successfully";
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {

        User loggedIn = userService.login(user.getEmail(), user.getPassword());

        if (loggedIn == null) {
            return "Invalid credentials";
        }

        return "Login successful for role: " + loggedIn.getRole();
    }
}
