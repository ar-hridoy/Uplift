package com.uplift.desktop.controller;

import com.uplift.desktop.api.ApiClient;
import com.uplift.desktop.dto.OtpSentResponse;
import com.uplift.desktop.dto.passenger.PassengerSignupDto;
import com.uplift.desktop.util.AlertUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class PassengerSignupController {

    @FXML
    private BorderPane rootPane;  // must match fx:id="rootPane" in passenger_signup.fxml

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private void handleCreateAccount() {
        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty()
                || password.isEmpty() || confirmPassword.isEmpty()) {
            AlertUtils.showError("Validation Error", "All fields are required.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            AlertUtils.showError("Password Mismatch", "Password and Confirm Password must match.");
            return;
        }

        PassengerSignupDto dto = new PassengerSignupDto();
        dto.setFullName(fullName);
        dto.setUsername(username);
        dto.setEmail(email);
        dto.setPassword(password);

        try {
            // CALL BACKEND → sends OTP
            OtpSentResponse resp = ApiClient.signupPassenger(dto);

            // Tell user (you can also use resp.getMessage() if it has one)
            AlertUtils.showInfo(
                    "Signup",
                    "OTP sent to your email. Please check your inbox."
            );

            // OPEN OTP SCREEN
            openOtpScreen(email);

        } catch (IOException e) {
            AlertUtils.showException(
                    "Network Error",
                    "Could not connect to server. Please try again.",
                    e
            );
        } catch (RuntimeException ex) {
            // This should be your friendly 400 mapping from HttpUtils.post (e.g. "Email already used")
            AlertUtils.showError("Signup Failed", ex.getMessage());
        }
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/login.fxml") // adjust path if needed
            );
            BorderPane loginRoot = loader.load();

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.centerOnScreen();
        } catch (IOException e) {
            AlertUtils.showException("Navigation Error",
                    "Cannot load login screen.",
                    e);
        }
    }

    // ---------- NAVIGATION TO OTP SCREEN ----------

    private void openOtpScreen(String email) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/auth/otp_verification.fxml")
                    // ⬆️ keep this if that is really the path to your OTP FXML
                    // or change to the correct one (e.g. "/fxml/otp_verification.fxml")
            );
            Parent root = loader.load();

            // Use your existing OtpVerificationController
            OtpVerificationController controller = loader.getController();
            controller.initForSignup(email);

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();

        } catch (IOException e) {
            AlertUtils.showException(
                    "Navigation Error",
                    "Could not open OTP verification screen.",
                    e
            );
        }
    }
}
