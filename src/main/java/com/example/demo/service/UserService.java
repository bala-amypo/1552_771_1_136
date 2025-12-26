package com.example.demo.service;

import com.example.demo.entity.User;

public interface UserService {
    /**
     * Registers a new user with BCrypt password hashing and role assignment.
     */
    User register(User user);

    /**
     * Retrieves a user by their unique ID.
     */
    User getById(Long id);

    /**
     * Finds a user by email for security and login processes.
     */
    User findByEmail(String email);
}