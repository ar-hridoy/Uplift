package com.uplift.desktop.controller;

import com.uplift.desktop.api.ApiClient;
import com.uplift.desktop.api.TokenManager;
import com.uplift.desktop.dto.LoginResponse;
import com.uplift.desktop.dto.OtpVerifyRequest;
import com.uplift.desktop.util.AlertUtils;
import com.uplift.desktop.controller.passenger.PassengerDashboardController;
import com.uplift.desktop.controller.rider.RiderDashboardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class OtpVerificationController {

    public enum Mode { SIGNUP, LOGIN, PASSWORD_RESET }

    @FXML private BorderPane rootPane;

    @FXML private Label titleLabel;
    @FXML private Label subtitleLabel;
    @FXML private Label emailLabel;

    @FXML private TextField otpField;

    @FXML private Button verifyButton;
    @FXML private Hyperlink resendLink;
    @FXML private Hyperlink backToLoginLink;

    private String email;
    private String portal;      // PASSENGER / RIDER (for LOGIN)
    private Mode mode = Mode.SIGNUP;

    public void initForSignup(String email) {
        this.mode = Mode.SIGNUP;
        this.email = email;

        titleLabel.setText("Verify Your Email");
        subtitleLabel.setText("Enter the code sent to your email to activate your account.");
        emailLabel.setText(email);
    }

    public void initForLogin(String email, String portal) {
        this.mode = Mode.LOGIN;
        this.email = email;
        this.portal = portal;

        titleLabel.setText("Login Verification");
        subtitleLabel.setText("Enter the code we emailed you to finish signing in.");
        emailLabel.setText(email + " (" + portal + ")");
    }

    // ✅ NEW
    public void initForPasswordReset(String email) {
        this.mode = Mode.PASSWORD_RESET;
        this.email = email;

        titleLabel.setText("Reset Verification");
        subtitleLabel.setText("Enter the reset code we sent to your email.");
        emailLabel.setText(email);
    }

    @FXML
    private void handleVerifyOtp(ActionEvent event) {
        String code = otpField.getText().trim();

        if (code.isEmpty()) {
            AlertUtils.showError("Invalid code", "Please enter the verification code.");
            return;
        }

        // ✅ PASSWORD RESET flow: just go next screen carrying email+code
        if (mode == Mode.PASSWORD_RESET) {
            openResetPasswordScreen(email, code);
            return;
        }

        // Otherwise SIGNUP/LOGIN use existing backend OTP verification
        OtpVerifyRequest req = new OtpVerifyRequest();
        req.setEmail(email);
        req.setCode(code);

        if (mode == Mode.LOGIN) {
            req.setPortal(portal);
        }

        try {
            if (mode == Mode.SIGNUP) {
                ApiClient.verifySignupOtp(req);

                AlertUtils.showInfo("Signup complete",
                        "Your email has been verified. You can now log in.");

                openLoginScreen();

            } else { // LOGIN
                LoginResponse resp = ApiClient.verifyLoginOtp(req);
                TokenManager.setToken(resp.getToken());
                routeAfterLogin(resp);
            }

        } catch (IOException e) {
            AlertUtils.showException("OTP Error",
                    "Could not verify the code. Please try again.", e);
        } catch (RuntimeException ex) {
            AlertUtils.showError("OTP Error", ex.getMessage());
        }
    }

    @FXML
    private void handleResendOtp(ActionEvent event) {
        // Optional: implement resend endpoints later.
        AlertUtils.showInfo("Not implemented", "Resend OTP is not implemented yet.");
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) {
        openLoginScreen();
    }

    private void openResetPasswordScreen(String email, String code) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/auth/reset_password.fxml")
            );
            BorderPane root = loader.load();

            ResetPasswordController controller = loader.getController();
            controller.init(email, code);

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
        } catch (IOException e) {
            AlertUtils.showException("Navigation Error",
                    "Could not open reset password screen.", e);
        }
    }

    private void openLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            BorderPane loginRoot = loader.load();

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.centerOnScreen();
        } catch (IOException e) {
            AlertUtils.showException("Navigation Error", "Cannot load login screen.", e);
        }
    }

    private void routeAfterLogin(LoginResponse resp) {
        String role = resp.getRole();

        if ("ADMIN".equalsIgnoreCase(role)) {
            openAdminDashboard(resp);
        } else if ("RIDER".equalsIgnoreCase(role)) {
            openRiderDashboard(resp);
        } else {
            openPassengerDashboard(resp);
        }
    }


    private void openPassengerDashboard(LoginResponse resp) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/passenger/passenger_dashboard.fxml")
            );
            BorderPane root = loader.load();

            PassengerDashboardController controller = loader.getController();
            controller.init(resp);

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
        } catch (IOException e) {
            AlertUtils.showException("Dashboard Error", "Unable to open passenger dashboard.", e);
        }
    }

    private void openRiderDashboard(LoginResponse resp) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/rider/rider_dashboard.fxml")
            );
            BorderPane root = loader.load();

            RiderDashboardController controller = loader.getController();
            try { controller.init(resp); } catch (Exception ignored) {}

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
        } catch (IOException e) {
            AlertUtils.showException("Dashboard Error", "Unable to open rider dashboard.", e);
        }
    }

    private void openAdminDashboard(LoginResponse resp) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/admin_dashboard.fxml"));
            BorderPane root = loader.load();

            com.uplift.desktop.controller.admin.AdminDashboardController controller = loader.getController();
            controller.init(resp); // ✅ THIS LINE is required

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("UpLift - Admin Panel");
            stage.centerOnScreen();
        } catch (Exception e) {
            AlertUtils.showException("Dashboard Error", "Unable to open admin dashboard.", e);
        }
    }


}
