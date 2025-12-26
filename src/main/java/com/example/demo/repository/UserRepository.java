package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds a user by their unique email address.
     * Used for authentication and registration checks[cite: 4, 34].
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user exists with the given email.
     * Used for duplicate email validation.
     */
    boolean existsByEmail(String email);
}