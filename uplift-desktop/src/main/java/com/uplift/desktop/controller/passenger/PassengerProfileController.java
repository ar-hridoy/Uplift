package com.uplift.desktop.controller.passenger;

import com.uplift.desktop.dto.LoginResponse;
import com.uplift.desktop.util.AlertUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class PassengerProfileController {

    @FXML private Label lblNameBig;
    @FXML private Label lblRoleBig;
    @FXML private Label lblEmailSmall;
    @FXML private Label lblActive;

    @FXML private TextField txtFullName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhone;
    @FXML private TextField txtRole;

    @FXML private PasswordField txtCurrentPassword;
    @FXML private PasswordField txtNewPassword;
    @FXML private PasswordField txtConfirmPassword;

    @FXML private Button btnSave;
    @FXML private Button btnReset;
    @FXML private Label lblStatus;
    @FXML private TextField txtAddress;


    private LoginResponse session;

    public void init(LoginResponse resp) {
        this.session = resp;

        String name = resp.getFullName() != null ? resp.getFullName() : "Passenger";
        String role = resp.getRole() != null ? resp.getRole() : "PASSENGER";
        String email = resp.getEmail() != null ? resp.getEmail() : "";

        lblNameBig.setText(name);
        lblRoleBig.setText(role);
        lblEmailSmall.setText(email);

        // You don’t have active in LoginResponse right now, so just show Active
        lblActive.setText("Active");

        txtFullName.setText(name);
        txtEmail.setText(email);
        txtRole.setText(role);
        txtAddress.setText(""); // address not loaded yet

    }

    @FXML
    private void handleSave(ActionEvent event) {
        // UI-only for now (no backend endpoint shown yet)
        String fullName = txtFullName.getText().trim();

        if (fullName.isEmpty()) {
            AlertUtils.showError("Validation", "Full name cannot be empty.");
            return;
        }

        // Password section validation (optional)
        String curr = txtCurrentPassword.getText();
        String n1 = txtNewPassword.getText();
        String n2 = txtConfirmPassword.getText();

        if (!n1.isBlank() || !n2.isBlank() || !curr.isBlank()) {
            if (curr.isBlank()) {
                AlertUtils.showError("Validation", "Current password is required to change password.");
                return;
            }
            if (n1.length() < 6) {
                AlertUtils.showError("Validation", "New password must be at least 6 characters.");
                return;
            }
            if (!n1.equals(n2)) {
                AlertUtils.showError("Validation", "New password and confirm password do not match.");
                return;
            }
        }

        lblStatus.setText("Saved locally (backend update not connected yet).");
        AlertUtils.showInfo("Saved", "Profile changes saved (UI only for now).");

        String address = txtAddress.getText().trim();
// optional validation:
// if (address.isEmpty()) { ... }

    }

    @FXML
    private void handleReset(ActionEvent event) {
        if (session != null) {
            init(session);
        }
        txtCurrentPassword.clear();
        txtNewPassword.clear();
        txtConfirmPassword.clear();
        lblStatus.setText("Reset.");
        txtAddress.clear();

    }
}
