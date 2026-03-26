package com.uplift.desktop.api;

import com.uplift.desktop.dto.LoginRequest;
import com.uplift.desktop.dto.LoginResponse;
import com.uplift.desktop.dto.OtpSentResponse;
import com.uplift.desktop.dto.OtpVerifyRequest;
import com.uplift.desktop.dto.passenger.PassengerSignupDto;
import com.uplift.desktop.dto.CreateRiderRequest;
import com.uplift.desktop.dto.UserCreatedResponse;


import java.io.IOException;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080";

    // =========================================================
    //                AUTH – LOGIN (2 STEP)
    // =========================================================

    /**
     * Step 1 – start login: backend checks password & sends OTP to email
     * POST /api/auth/login/start
     */
    public static OtpSentResponse startLogin(LoginRequest req) throws IOException {
        String url = BASE_URL + "/api/auth/login/start";
        String jsonBody = HttpUtils.toJson(req);

        try {
            String respBody = HttpUtils.post(url, jsonBody, null);
            return HttpUtils.fromJson(respBody, OtpSentResponse.class);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Login start request interrupted", e);
        }
    }

    /**
     * Step 2 – verify login OTP: returns JWT + user info
     * POST /api/auth/login/verify-otp
     */
    public static LoginResponse verifyLoginOtp(OtpVerifyRequest req) throws IOException {
        String url = BASE_URL + "/api/auth/login/verify-otp";
        String jsonBody = HttpUtils.toJson(req);
        try {
            String respBody = HttpUtils.post(url, jsonBody, null);
            return HttpUtils.fromJson(respBody, LoginResponse.class);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Verify login OTP interrupted", e);
        }
    }

    public static UserCreatedResponse createRider(CreateRiderRequest req, String token) throws IOException {
        String url = BASE_URL + "/api/admin/riders";
        String jsonBody = HttpUtils.toJson(req);

        try {
            String respBody = HttpUtils.post(url, jsonBody, token); // must attach Bearer
            return HttpUtils.fromJson(respBody, UserCreatedResponse.class);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Create rider interrupted", e);
        }
    }



    // =========================================================
    //            AUTH – PASSENGER SIGNUP (2 STEP)
    // =========================================================

    /**
     * Step 1 – signup: creates inactive user & sends OTP
     * POST /api/auth/signup/passenger
     */
    public static OtpSentResponse signupPassenger(PassengerSignupDto dto) throws IOException {
        String url = BASE_URL + "/api/auth/signup/passenger";
        String jsonBody = HttpUtils.toJson(dto);

        try {
            String respBody = HttpUtils.post(url, jsonBody, null);
            return HttpUtils.fromJson(respBody, OtpSentResponse.class);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Passenger signup request interrupted", e);
        }
    }

    /**
     * Step 2 – verify signup OTP: activates user, may return token
     * POST /api/auth/signup/passenger/verify-otp
     */
    public static LoginResponse verifySignupOtp(OtpVerifyRequest req) throws IOException {
        String url = BASE_URL + "/api/auth/signup/passenger/verify-otp";
        String jsonBody = HttpUtils.toJson(req);

        try {
            String respBody = HttpUtils.post(url, jsonBody, null);
            return HttpUtils.fromJson(respBody, LoginResponse.class);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Verify signup OTP interrupted", e);
        }
    }

    // =========================================================
    //                GENERIC HELPERS
    // =========================================================

    public static String get(String url, String token) throws IOException {
        return HttpUtils.get(url, token);
    }

    public static String getRelative(String path, String token) throws IOException {
        String url = BASE_URL + path;
        return HttpUtils.get(url, token);
    }

    public static String post(String path, String jsonBody, String token) throws IOException {
        String url = BASE_URL + path;
        try {
            return HttpUtils.post(url, jsonBody, token);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("POST " + path + " interrupted", e);
        }
    }
    // ========================================================= forget password steps ===============
    public static void startPasswordReset(String email) throws IOException {
        String url = BASE_URL + "/api/auth/password/reset/start";
        String body = HttpUtils.toJson(java.util.Map.of("email", email));
        try {
            HttpUtils.post(url, body, null);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException(e);
        }
    }

    public static void confirmPasswordReset(String email, String code, String newPassword) throws IOException {
        String url = BASE_URL + "/api/auth/password/reset/confirm";
        String body = HttpUtils.toJson(java.util.Map.of(
                "email", email,
                "code", code,
                "newPassword", newPassword
        ));
        try {
            HttpUtils.post(url, body, null);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException(e);
        }
    }


}
