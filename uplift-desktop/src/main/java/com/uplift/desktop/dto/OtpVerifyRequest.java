package com.uplift.desktop.dto;

public class OtpVerifyRequest {

    private String email;   // MUST be called 'email' to match backend DTO
    private String code;
    private String portal;  // only used for login; can be null for signup

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getPortal() {
        return portal;
    }
    public void setPortal(String portal) {
        this.portal = portal;
    }
}
