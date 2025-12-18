package com.example.loan.service;

import com.example.loan.entity.User;

public interface UserService {
    User registerUser(User user);
    User getUserById(Long id);
    User findByEmail(String email);
}
