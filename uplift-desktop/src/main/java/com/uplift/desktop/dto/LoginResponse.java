package com.uplift.desktop.dto;

public class LoginResponse {

    private String token;
    private String fullName;
    private String email;
    private String role;

    public LoginResponse() {
    }

    public String getToken() {
        return token;
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
}
