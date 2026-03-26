package com.uplift.desktop.controller;

import com.uplift.desktop.api.ApiClient;
import com.uplift.desktop.util.AlertUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ForgotPasswordController {

    @FXML private BorderPane rootPane;
    @FXML private TextField emailField;

    @FXML
    private void handleSendCode(ActionEvent event) {
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            AlertUtils.showError("Validation", "Email is required.");
            return;
        }

        try {
            ApiClient.startPasswordReset(email);
            AlertUtils.showInfo("OTP Sent", "We sent a reset code to your email.");

            openOtpScreenForReset(email);

        } catch (Exception e) {
            AlertUtils.showError("Reset Failed", e.getMessage());
        }
    }

    private void openOtpScreenForReset(String email) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/auth/otp_verification.fxml")
            );
            BorderPane root = loader.load();

            OtpVerificationController controller = loader.getController();
            controller.initForPasswordReset(email);

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();

        } catch (IOException e) {
            AlertUtils.showException("Navigation Error", "Cannot open OTP screen.", e);
        }
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) {
        try {
            BorderPane loginRoot = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.centerOnScreen();
        } catch (IOException e) {
            AlertUtils.showException("Navigation Error", "Cannot open login.", e);
        }
    }
}
