package com.uplift.desktop.dto;

public class LoginRequest {
    private String usernameOrEmail;
    private String password;
    private String portal;

    public String getUsernameOrEmail() { return usernameOrEmail; }
    public void setUsernameOrEmail(String usernameOrEmail) { this.usernameOrEmail = usernameOrEmail; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPortal() { return portal; }
    public void setPortal(String portal) { this.portal = portal; }
}
