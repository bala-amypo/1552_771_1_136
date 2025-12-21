package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Finds a user by their unique email for login/registration checks
    Optional<User> findByEmail(String email);
    
    // Checks if a user already exists with this email
    boolean existsByEmail(String email);
}