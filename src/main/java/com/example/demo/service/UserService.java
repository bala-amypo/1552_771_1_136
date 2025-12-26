// src/main/java/com/example/demo/service/UserService.java
package com.example.demo.service;

import com.example.demo.entity.User;

public interface UserService {
    User register(User user);
    User getById(Long id);
    User findByEmail(String email);
}
