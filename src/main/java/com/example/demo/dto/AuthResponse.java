package com.example.demo.dto;

public class AuthResponse {
    private Long userId;
    private String email;
    private String fullName;
    private String role;  // <- Add this field
    private String token;

    // existing getters/setters ...

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
