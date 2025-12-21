package com.example.demo.service;
import com.example.demo.entity.User;
import java.util.List;

public interface UserService {
    User register(User user);
    User getById(Long id);
    User findByEmail(String email);
    List<User> getAllUsers();
}