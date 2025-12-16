package com.example.demo.entity;

import java.security.Timestamp;

public class User {
    private long Id;
    private String fullName;
    private String email;
    private String role;
    private Timestamp createdAt;

    public User(){

    }

    public User(long id, String fullName, String email, String role, Timestamp createdAt) {
        Id = id;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }

    public long getId() {
        return Id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setId(long id) {
        Id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }


}
