package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.exception.*;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserServiceImpl implements UserService {

    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserServiceImpl(UserRepository repo) {
        this.repo = repo;
    }

    public User register(User u) {
        if (repo.findByEmail(u.getEmail()).isPresent())
            throw new BadRequestException("Email exists");

        u.setPassword(encoder.encode(u.getPassword()));
        u.setRole(User.Role.CUSTOMER.name());
        return repo.save(u);
    }

    public User getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User findByEmail(String email) {
        return repo.findByEmail(email).orElse(null);
    }
}
