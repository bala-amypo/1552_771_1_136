package com.example.demo.entity;


import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "users")
public class User {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;


@Column(nullable = false)
private String fullName;


@Column(nullable = false, unique = true)
private String email;


@Column(nullable = false)
private String password;


@Column(nullable = false)
private String role = "CUSTOMER";


private LocalDateTime createdAt;


@PrePersist
void onCreate() {
this.createdAt = LocalDateTime.now();
}


public User() {}


public User(String fullName, String email, String password, String role) {
this.fullName = fullName;
this.email = email;
this.password = password;
this.role = role;
}


// getters and setters
}