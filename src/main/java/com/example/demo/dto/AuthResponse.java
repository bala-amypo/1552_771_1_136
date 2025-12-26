package com.example.demo.dto;

public class AuthResponse {
    private Long userId;
    private String email;
    private String fullName;
    private String token;

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
