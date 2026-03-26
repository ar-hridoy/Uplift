package com.uplift.backend.dto.auth;

public class LoginDto {
    private String usernameOrEmail;
    private String password;
    private String portal; // "RIDER", "PASSENGER", or maybe "ADMIN"

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPortal() {
        return portal;
    }

    public void setPortal(String portal) {
        this.portal = portal;
    }
}
