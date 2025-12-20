package com.example.demo.service;

import com.example.demo.entity.User;

public interface UserService {

    /**
     * Registers a new user
     * @param user User entity
     * @return saved User
     * @throws BadRequestException if email already exists
     */
    User register(User user);

    /**
     * Get user by ID
     * @param id user id
     * @return User
     */
    User getById(Long id);

    /**
     * Find user by email
     * @param email email
     * @return User
     */
    User findByEmail(String email);
}
