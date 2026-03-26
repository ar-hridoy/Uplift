package com.uplift.backend.dto.admin;

public class UserCreatedResponse {
    private Long id;
    private String fullName;
    private String username;
    private String email;
    private String role;
    private String status;

    public UserCreatedResponse(Long id, String fullName, String username, String email, String role, String status) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.role = role;
        this.status = status;
    }

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getStatus() { return status; }
}
