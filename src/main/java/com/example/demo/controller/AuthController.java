package com.example.loan.controller;

import com.example.loan.entity.User;
import com.example.loan.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) { this.service = service; }

    @PostMapping
    public User register(@RequestBody User user) { return service.registerUser(user); }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) { return service.getUserById(id); }
}
