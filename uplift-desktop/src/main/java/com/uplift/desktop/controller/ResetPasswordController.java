package com.uplift.desktop.controller;

import com.uplift.desktop.api.ApiClient;
import com.uplift.desktop.util.AlertUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ResetPasswordController {

    @FXML private BorderPane rootPane;
    @FXML private Label emailLabel;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;

    private String email;
    private String code; // OTP carried from OTP screen

    public void init(String email, String code) {
        this.email = email;
        this.code = code;
        emailLabel.setText(email);
    }

    @FXML
    private void handleChangePassword(ActionEvent event) {
        String p1 = newPasswordField.getText();
        String p2 = confirmPasswordField.getText();

        if (p1.isEmpty() || p2.isEmpty()) {
            AlertUtils.showError("Validation", "Please fill both password fields.");
            return;
        }
        if (!p1.equals(p2)) {
            AlertUtils.showError("Validation", "Passwords do not match.");
            return;
        }
        if (p1.length() < 6) {
            AlertUtils.showError("Validation", "Password must be at least 6 characters.");
            return;
        }

        try {
            ApiClient.confirmPasswordReset(email, code, p1);

            AlertUtils.showInfo("Success", "Password changed. You can now log in.");
            openLogin();

        } catch (Exception e) {
            AlertUtils.showError("Reset Failed", e.getMessage());
        }
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) {
        openLogin();
    }

    private void openLogin() {
        try {
            BorderPane loginRoot = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.centerOnScreen();
        } catch (IOException e) {
            AlertUtils.showException("Navigation Error", "Cannot open login screen.", e);
        }
    }
}
