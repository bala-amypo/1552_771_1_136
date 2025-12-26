// src/main/java/com/example/demo/dto/AuthResponse.java
package com.example.demo.dto;

public class AuthResponse {
    private String token;
    private Long userId;
    private String email;
    private String role;
    private String fullName;

    public AuthResponse() {}
    public AuthResponse(String token, Long userId, String email, String role, String fullName) {
        this.token = token; this.userId = userId; this.email = email; this.role = role; this.fullName = fullName;
    }
    public String getToken() { return token; }
    public Long getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getFullName() { return fullName; }
    public void setToken(String token) { this.token = token; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(String role) { this.role = role; }
    public void setFullName(String fullName) { this.fullName = fullName; }
}
