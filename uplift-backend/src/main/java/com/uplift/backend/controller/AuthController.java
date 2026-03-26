package com.uplift.backend.controller;

import com.uplift.backend.dto.auth.*;
import com.uplift.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // LOGIN NORMAL (not OTP)
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    // ✅ SIGNUP — SEND OTP TO EMAIL
    @PostMapping("/signup/passenger")
    public ResponseEntity<OtpSentResponse> signupPassenger(
            @Valid @RequestBody RegisterPassengerDto dto) {

        return ResponseEntity.ok(authService.startPassengerSignup(dto));
    }

    // VERIFY SIGNUP OTP
    @PostMapping("/signup/passenger/verify-otp")
    public ResponseEntity<LoginResponse> verifySignupOtp(
            @Valid @RequestBody VerifySignupOtpDto dto) {

        return ResponseEntity.ok(authService.verifySignupOtp(dto));
    }

    // LOGIN (STEP 1: send OTP)
    @PostMapping("/login/start")
    public ResponseEntity<OtpSentResponse> startLogin(@Valid @RequestBody LoginDto dto) {
        return ResponseEntity.ok(authService.startLogin(dto));
    }

    // LOGIN (STEP 2: verify OTP)
    @PostMapping("/login/verify-otp")
    public ResponseEntity<LoginResponse> verifyLoginOtp(
            @Valid @RequestBody VerifyLoginOtpDto dto) {

        return ResponseEntity.ok(authService.verifyLoginOtp(dto));
    }

    @PostMapping("/password/reset/start")
    public ResponseEntity<OtpSentResponse> startReset(@Valid @RequestBody ForgotPasswordStartDto dto) {
        return ResponseEntity.ok(authService.startPasswordReset(dto.getEmail()));
    }

    @PostMapping("/password/reset/verify-otp")
    public ResponseEntity<?> verifyResetOtp(@Valid @RequestBody VerifyResetOtpDto dto) {
        authService.verifyPasswordResetOtp(dto.getEmail(), dto.getCode());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/reset/confirm")
    public ResponseEntity<?> confirmReset(@Valid @RequestBody ResetPasswordDto dto) {
        authService.resetPassword(dto.getEmail(), dto.getCode(), dto.getNewPassword());
        return ResponseEntity.ok().build();
    }

}
