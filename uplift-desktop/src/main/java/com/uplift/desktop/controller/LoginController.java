package com.uplift.desktop.controller;

import com.uplift.desktop.api.ApiClient;
import com.uplift.desktop.dto.LoginRequest;
import com.uplift.desktop.dto.LoginResponse;
import com.uplift.desktop.dto.OtpVerifyRequest;
import com.uplift.desktop.util.AlertUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    // === FXML fields (MUST match login.fxml) ===
    @FXML private BorderPane rootPane;

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    @FXML private RadioButton passengerRadio;
    @FXML private RadioButton riderRadio;

    @FXML private CheckBox rememberMeCheckBox;
    @FXML private Hyperlink forgotPasswordLink;
    @FXML private Hyperlink signupLink;

    @FXML private Button signInButton;

    @FXML private ToggleGroup userRoleToggleGroup;
    // =========================================

    @FXML
    private void initialize() {
        // Ensure toggle group exists
        if (userRoleToggleGroup == null) {
            userRoleToggleGroup = new ToggleGroup();
        }

        if (passengerRadio != null) {
            passengerRadio.setToggleGroup(userRoleToggleGroup);
        }
        if (riderRadio != null) {
            riderRadio.setToggleGroup(userRoleToggleGroup);
        }

        // Default role = PASSENGER
        if (passengerRadio != null) {
            passengerRadio.setSelected(true);
        }
    }

    // Called when Passenger / Rider radio clicked
    @FXML
    private void onRoleToggle() {
        // Optional: debug/log
        // RadioButton selected = (RadioButton) userRoleToggleGroup.getSelectedToggle();
        // System.out.println("Selected role: " + selected.getText());
    }

    @FXML
    private void onForgotPassword() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/auth/forgot_password.fxml"));
            BorderPane root = loader.load();

            Stage stage = (Stage) signInButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
        } catch (IOException e) {
            AlertUtils.showException("Navigation Error", "Cannot open forgot password screen.", e);
        }
    }



    @FXML
    private void openSignup(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/passenger/passenger_signup.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) signInButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("UpLift - Passenger Sign Up");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Unable to open Passenger Sign Up screen.");
        }
    }

    // 🔑 This must match onAction="#handleSignIn" in FXML
   @FXML
    private void handleSignIn(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            AlertUtils.showError("Validation Error", "Email and password are required.");
            return;
        }

        // UI selection → portal string
        String portal = riderRadio.isSelected() ? "RIDER" : "PASSENGER";

        try {
            LoginRequest req = new LoginRequest();
            req.setUsernameOrEmail(email);
            req.setPassword(password);
            req.setPortal(portal);

            ApiClient.startLogin(req);

            AlertUtils.showInfo("OTP sent", "We sent a login verification code to your email.");
            openOtpScreenForLogin(email, portal);

        } catch (Exception ex) {
            AlertUtils.showError("Login Failed", ex.getMessage());
        }
    }



    // Helper: map selected radio button to portal string for backend
    private String getSelectedPortal() {
        if (userRoleToggleGroup != null && userRoleToggleGroup.getSelectedToggle() != null) {
            RadioButton selected = (RadioButton) userRoleToggleGroup.getSelectedToggle();
            String txt = selected.getText().trim().toUpperCase(); // "PASSENGER" or "RIDER"
            if (txt.startsWith("PASSENGER")) return "PASSENGER";
            if (txt.startsWith("RIDER")) return "RIDER";
        }
        // default
        return "PASSENGER";
    }

    // Called after OTP verification (from OtpVerificationController) if role = PASSENGER
    private void openPassengerDashboard(LoginResponse resp) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/passenger/passenger_dashboard.fxml")
            );
            BorderPane root = loader.load();

            com.uplift.desktop.controller.passenger.PassengerDashboardController controller =
                    loader.getController();
            controller.init(resp);

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
        } catch (Exception e) {
            AlertUtils.showException("Dashboard Error",
                    "Unable to open passenger dashboard.",
                    e);
        }
    }

    // Called after OTP verification (from OtpVerificationController) if role = RIDER
    private void openRiderDashboard(LoginResponse resp) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/rider/rider_dashboard.fxml"));
            BorderPane root = loader.load();

            // If you have RiderDashboardController.init(resp), call it here similarly
            // RiderDashboardController controller = loader.getController();
            // controller.init(resp);

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Unable to open Rider Dashboard.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    // Open OTP verification screen in LOGIN mode
    private void openOtpScreenForLogin(String email, String portal) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/auth/otp_verification.fxml")
            );
            BorderPane root = loader.load();

            com.uplift.desktop.controller.OtpVerificationController controller =
                    loader.getController();
            controller.initForLogin(email, portal);

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
