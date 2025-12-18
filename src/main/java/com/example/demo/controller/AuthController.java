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

    public AuthController(UserServiceImpl userService, BCryptPasswordEncoder passwordEncoder){
        this.userService=userService;
        this.passwordEncoder=passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user){
        User saved=userService.registerUser(user);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody Map<String,String> authRequest){
        User user=userService.findByEmail(authRequest.get("email"));
        if(!passwordEncoder.matches(authRequest.get("password"), user.getPassword())){
            throw new BadRequestException("Invalid email or password");
        }
        Map<String,String> response=new HashMap<>();
        response.put("token","dummy-jwt-token-for-"+user.getEmail());
        return ResponseEntity.ok(response);
    }
}
