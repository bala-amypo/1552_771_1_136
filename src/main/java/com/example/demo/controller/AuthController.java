package com.example.loan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.loan.entity.User;
import com.example.loan.service.UserService;

import io.swagger.v3.oas.annotation

@RestController
@RequestMapping("/auth")
@Tag(name="Loan Eligibility", description="Loan eligibility check")
public class AuthController {
    
    @Autowired
    UserService userService;

    @PostMapping("/{register}")
    public User register(@RequestBody User user){
        return userService.createUser(user);
    }

   @PostMapping("/login")
    public User login(@RequestBody User user) {
        return userService.findByEmail(user.getEmail());
    }
}
