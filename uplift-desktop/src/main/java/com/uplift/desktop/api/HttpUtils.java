package com.uplift.desktop.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    // ✅ Convert any Java object to JSON string
    public static String toJson(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (IOException e) {
            throw new RuntimeException("Failed to serialize object to JSON", e);
        }
    }

    // ✅ POST with optional Bearer token
    public static String post(String url, String jsonBody, String bearerToken)
            throws IOException, InterruptedException {

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody));

        if (bearerToken != null && !bearerToken.isBlank()) {
            builder.header("Authorization", "Bearer " + bearerToken);
        }

        HttpRequest request = builder.build();

        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        int status = response.statusCode();
        String body = response.body();

        if (status >= 200 && status < 300) {
            return body;
        }

// 🔹 Default error message
        String friendlyMessage = "HTTP " + status;

        // ✅ Improve login-friendly messages too
        if (status == 401 && body != null && body.toLowerCase().contains("invalid credentials")) {
            friendlyMessage = "Invalid email or password.";
        }
        if (status == 403 && body != null && body.toLowerCase().contains("cannot log into")) {
            friendlyMessage = "This account cannot log into the selected portal.";
        }
        if (status == 403 && body != null && body.toLowerCase().contains("not verified")) {
            friendlyMessage = "Account not verified. Please complete OTP verification first.";
        }

        if (status == 400) {
            if (body.contains("Email already used")) {
                friendlyMessage = "This email is already registered. Please use another email.";
            } else if (body.contains("Username already used")) {
                friendlyMessage = "This username is already taken. Please choose another username.";
            } else if (body.contains("must be a well-formed email address")) {
                friendlyMessage = "Please enter a valid email address.";
            } else if (body.contains("size must be between") && body.contains("password")) {
                friendlyMessage = "Password must be at least 6 characters.";
            }
        }

        throw new RuntimeException(friendlyMessage);
    }

    // ✅ Parse JSON string into a Java object
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse JSON", e);
        }
    }
    public static String get(String url, String token) throws IOException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url));

        if (token != null && !token.isBlank()) {
            builder.header("Authorization", "Bearer " + token);
        }

        HttpRequest request = builder.GET().build();

        try {
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                throw new RuntimeException("HTTP " + response.statusCode() + " - " + response.body());
            }

            return response.body();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("GET request interrupted", e);
        }
    }



}
