package com.uplift.backend.service;

import com.uplift.backend.config.JwtService;
import com.uplift.backend.dto.auth.LoginDto;
import com.uplift.backend.dto.auth.LoginResponse;
import com.uplift.backend.dto.auth.RegisterPassengerDto;
import com.uplift.backend.entity.User;
import com.uplift.backend.enums.UserRole;
import com.uplift.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.uplift.backend.dto.auth.OtpSentResponse;
import com.uplift.backend.dto.auth.VerifySignupOtpDto;
import com.uplift.backend.dto.auth.VerifyLoginOtpDto;
import com.uplift.backend.entity.EmailOtpToken;
import com.uplift.backend.enums.OtpPurpose;
import com.uplift.backend.repository.EmailOtpTokenRepository;
import com.uplift.backend.util.OtpGenerator;
import java.time.LocalDateTime;


@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final EmailOtpTokenRepository otpRepository;
    private final EmailService emailService;

    @Value("${uplift.otp.expiration-minutes:5}")
    private int otpExpirationMinutes;


    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       EmailOtpTokenRepository otpRepository,
                       EmailService emailService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.otpRepository = otpRepository;
        this.emailService = emailService;
    }


    // ---------------------- LOGIN ----------------------
    public LoginResponse login(LoginDto dto) {

        User user = userRepository.findByEmail(dto.getUsernameOrEmail())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        String portal = dto.getPortal();
        if (portal != null && !portal.trim().isEmpty()) {
            if (!user.getRole().name().equalsIgnoreCase(portal)) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "This account cannot log into the " + portal + " portal."
                );
            }
        }

        String token = jwtService.generateToken(user);

        return new LoginResponse(
                token,
                user.getRole().name(),
                user.getFullName(),
                user.getEmail()
        );
    }

    // ---------------------- REGISTER PASSENGER ----------------------
    public void registerPassenger(RegisterPassengerDto dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
        }

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already taken");
        }

        User user = new User();
        user.setFullName(dto.getFullName());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setRole(UserRole.PASSENGER);
        user.setActive(true);

        userRepository.save(user);
    }

    public OtpSentResponse startPassengerSignup(RegisterPassengerDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already used");
        }
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already used");
        }

        User user = new User();
        user.setFullName(dto.getFullName());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setRole(UserRole.PASSENGER);
        user.setActive(false); // IMPORTANT
        user = userRepository.save(user);

        String code = OtpGenerator.generate6DigitCode();

        EmailOtpToken otp = new EmailOtpToken();
        otp.setUser(user);
        otp.setCode(code);
        otp.setPurpose(OtpPurpose.SIGNUP);
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(otpExpirationMinutes));
        otpRepository.save(otp);

        String emailBody = "Dear User,\n\n" +
                "Your Uplift one-time verification code is: **" + code + "**\n\n" +
                "For your safety, please keep this code confidential.\n" +
                "This code will expire in 5 minutes.\n\n" +
                "Thank you for choosing Uplift!\n\n" +
                "Best regards,\n" +
                "The Uplift Team";

        emailService.sendOtpEmail(
                user.getEmail(),
                "Uplift Signup Verification Code",
                emailBody
        );
        return new OtpSentResponse("Signup OTP sent to email.");
    }

    public LoginResponse verifySignupOtp(VerifySignupOtpDto dto) {

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

        EmailOtpToken otp = otpRepository
                .findTopByUserAndPurposeAndUsedFalseOrderByIdDesc(user, OtpPurpose.SIGNUP)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP not found"));

        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP expired");
        }

        if (!otp.getCode().equals(dto.getCode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid OTP");
        }

        otp.setUsed(true);
        otpRepository.save(otp);

        user.setActive(true);
        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return new LoginResponse(token, user.getRole().name(), user.getFullName(), user.getEmail());
    }
    public OtpSentResponse startLogin(LoginDto dto) {


        User user = userRepository.findByEmail(dto.getUsernameOrEmail())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        if (!user.isActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Account not verified");
        }

        // ✅ PORTAL CHECK HERE
        enforcePortal(user, dto.getPortal());

        String code = OtpGenerator.generate6DigitCode();

        EmailOtpToken otp = new EmailOtpToken();
        otp.setUser(user);
        otp.setCode(code);
        otp.setPurpose(OtpPurpose.LOGIN);
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(otpExpirationMinutes));
        otpRepository.save(otp);

        String emailBody = "Dear User,\n\n" +
                "Your Uplift login one-time verification code is: " + code + "\n\n" +
                "For your safety, please keep this code confidential.\n" +
                "This code will expire in 5 minutes.\n\n" +
                "Thank you for choosing Uplift!\n\n" +
                "Best regards,\n" +
                "The Uplift Team";

        emailService.sendOtpEmail(
                user.getEmail(),
                "Uplift Login Verification Code",
                emailBody
        );


        return new OtpSentResponse("Login OTP sent to email.");
    }

    public LoginResponse verifyLoginOtp(VerifyLoginOtpDto dto) {

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

        EmailOtpToken otp = otpRepository
                .findTopByUserAndPurposeAndUsedFalseOrderByIdDesc(user, OtpPurpose.LOGIN)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP not found"));

        if (!otp.getCode().equals(dto.getCode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid OTP");
        }

        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP expired");
        }

        otp.setUsed(true);
        otpRepository.save(otp);

        String token = jwtService.generateToken(user);

        return new LoginResponse(token, user.getRole().name(), user.getFullName(), user.getEmail());
    }



    private void enforcePortal(User user, String portal) {
        if (portal == null || portal.trim().isEmpty()) return;

        // 🔥 ADMIN can log in from any portal
        if (user.getRole() == UserRole.ADMIN) return;

        if (!user.getRole().name().equalsIgnoreCase(portal)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "This account cannot log into the " + portal + " portal."
            );
        }
    }
    public OtpSentResponse startPasswordReset(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

        String code = OtpGenerator.generate6DigitCode();

        EmailOtpToken otp = new EmailOtpToken();
        otp.setUser(user);
        otp.setCode(code);
        otp.setPurpose(OtpPurpose.PASSWORD_RESET);
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(otpExpirationMinutes));
        otpRepository.save(otp);

        String emailBody = "Dear User,\n\n" +
                "Your Uplift password reset code is: " + code + "\n\n" +
                "For your safety, please keep this code confidential.\n" +
                "This code will expire in 5 minutes.\n\n" +
                "If you did not request a password reset, please ignore this email.\n\n" +
                "Thank you for choosing Uplift!\n\n" +
                "Best regards,\n" +
                "The Uplift Team";

        emailService.sendOtpEmail(
                user.getEmail(),
                "Uplift Password Reset Code",
                emailBody
        );


        return new OtpSentResponse("Password reset OTP sent to email.");
    }

    public void verifyPasswordResetOtp(String email, String code) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

        EmailOtpToken otp = otpRepository
                .findTopByUserAndPurposeAndUsedFalseOrderByIdDesc(user, OtpPurpose.PASSWORD_RESET)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP not found"));

        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP expired");
        }
        if (!otp.getCode().equals(code)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid OTP");
        }

        // don’t mark used here if you want to allow retry on reset screen,
        // but safest is mark used ONLY when password is changed.
    }

    public void resetPassword(String email, String code, String newPassword) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

        EmailOtpToken otp = otpRepository
                .findTopByUserAndPurposeAndUsedFalseOrderByIdDesc(user, OtpPurpose.PASSWORD_RESET)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP not found"));

        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP expired");
        }
        if (!otp.getCode().equals(code)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid OTP");
        }

        otp.setUsed(true);
        otpRepository.save(otp);

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }



}
