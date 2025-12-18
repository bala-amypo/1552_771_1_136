package com.example.demo.service;

import com.example.demo.entity.User;
import java.util.List;

public interface UserService {
    User register(User user);  // <-- method name must match the controller call
    User getUserById(Long id);
    List<User> getAllUsers();
}
