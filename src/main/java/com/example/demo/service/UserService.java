package com.example.demo.service;

import com.example.demo.entity.User;

public interface UserService {
    User registerUser(User user);
    User getUserById(Long id);
    User findByEmail(String email);
}
