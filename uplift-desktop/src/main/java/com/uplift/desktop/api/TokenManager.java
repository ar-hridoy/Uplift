package com.uplift.desktop.api;

public class TokenManager {

    private static String token;
    private static String role;
    private static String fullName;
    private static String email;

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        TokenManager.token = token;
    }

    public static String getRole() {
        return role;
    }

    public static void setRole(String role) {
        TokenManager.role = role;
    }

    public static String getFullName() {
        return fullName;
    }

    public static void setFullName(String fullName) {
        TokenManager.fullName = fullName;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        TokenManager.email = email;
    }

    public static void clear() {
        token = null;
        role = null;
        fullName = null;
        email = null;
    }
}
