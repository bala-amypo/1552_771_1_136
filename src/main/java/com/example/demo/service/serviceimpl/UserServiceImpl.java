package com.example.loan.service.impl;

import com.example.loan.entity.User;
import com.example.loan.exception.*;
import com.example.loan.repository.UserRepository;
import com.example.loan.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repo;

    public UserServiceImpl(UserRepository repo) { this.repo = repo; }

    public User registerUser(User user) {
        if(repo.existsByEmail(user.getEmail()))
            throw new DuplicateRecordException("Email already exists");
        return repo.save(user);
    }

    public User getUserById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User findByEmail(String email) {
        return repo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
