package com.example.demo.serviceimpl;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private final Map<String, User> users = new HashMap<>();
    private long idCounter = 1;

    @Override
    public User register(User user) {

        if (users.containsKey(user.getEmail())) {
            return null; // email already exists
        }

        user.setId(idCounter++);
        users.put(user.getEmail(), user);
        return user;
    }

    @Override
    public User login(String email, String password) {

        User user = users.get(email);

        if (user == null || !user.getPassword().equals(password)) {
            return null;
        }

        return user;
    }
}
