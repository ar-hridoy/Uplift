package com.uplift.desktop.controller.admin;

import com.uplift.desktop.api.ApiClient;
import com.uplift.desktop.api.TokenManager;
import com.uplift.desktop.dto.CreateRiderRequest;
import com.uplift.desktop.dto.UserCreatedResponse;
import com.uplift.desktop.util.AlertUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminCreateRiderController {

    @FXML private TextField txtFullName;
    @FXML private TextField txtUsername;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhone;
    @FXML private TextField txtAddress;
    @FXML private PasswordField txtPassword;
    @FXML private CheckBox chkActive;
    @FXML private Label lblStatus;

    // Preview labels (must exist in FXML)
    @FXML private Label lblPreviewName;
    @FXML private Label lblPreviewEmail;
    @FXML private Label lblPreviewUsername;
    @FXML private Label lblPreviewPhone;
    @FXML private Label lblPreviewStatus;

    private UserCreatedResponse created; // result for caller

    public UserCreatedResponse getCreated() {
        return created;
    }

    @FXML
    private void initialize() {
        // Guard if preview nodes aren’t present (prevents NPE)
        boolean hasPreview =
                lblPreviewName != null && lblPreviewEmail != null &&
                        lblPreviewUsername != null && lblPreviewPhone != null &&
                        lblPreviewStatus != null;

        if (hasPreview) {
            txtFullName.textProperty().addListener((o, a, b) ->
                    lblPreviewName.setText(b == null || b.isBlank() ? "New Rider" : b));

            txtEmail.textProperty().addListener((o, a, b) ->
                    lblPreviewEmail.setText(b == null || b.isBlank() ? "-" : b));

            txtUsername.textProperty().addListener((o, a, b) ->
                    lblPreviewUsername.setText(b == null || b.isBlank() ? "-" : b));

            txtPhone.textProperty().addListener((o, a, b) ->
                    lblPreviewPhone.setText(b == null || b.isBlank() ? "-" : b));

            chkActive.selectedProperty().addListener((o, a, b) -> applyStatusStyle(b));
        }

        // default ACTIVE
        chkActive.setSelected(true);
        if (hasPreview) applyStatusStyle(true);
    }

    private void applyStatusStyle(boolean active) {
        lblPreviewStatus.getStyleClass().removeAll("status-badge-active", "status-badge-inactive");
        if (active) {
            lblPreviewStatus.setText("ACTIVE");
            lblPreviewStatus.getStyleClass().add("status-badge-active");
        } else {
            lblPreviewStatus.setText("INACTIVE");
            lblPreviewStatus.getStyleClass().add("status-badge-inactive");
        }
    }

    @FXML
    private void handleCreate(ActionEvent event) {
        String fullName = txtFullName.getText().trim();
        String username = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        String address = txtAddress.getText().trim();
        String password = txtPassword.getText();

        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            AlertUtils.showError("Validation", "Full name, username, email and password are required.");
            return;
        }

        CreateRiderRequest req = new CreateRiderRequest();
        req.setFullName(fullName);
        req.setUsername(username);
        req.setEmail(email);
        req.setPhone(phone);
        req.setAddress(address);          // ✅ ADD THIS (must exist in DTO)
        req.setPassword(password);
        req.setActive(chkActive.isSelected());

        try {
            String token = TokenManager.getToken();
            if (token == null || token.isBlank()) {
                AlertUtils.showError("Not logged in", "Admin token missing. Please login again.");
                return;
            }

            String jsonBody = com.uplift.desktop.api.HttpUtils.toJson(req);
            String respBody = ApiClient.post("/api/admin/riders", jsonBody, token);

            created = com.uplift.desktop.api.HttpUtils.fromJson(respBody, UserCreatedResponse.class);

            AlertUtils.showInfo("Created", "Rider account created successfully.");
            close(event);

        } catch (RuntimeException ex) {
            AlertUtils.showError("Create Rider Failed", ex.getMessage());
        } catch (IOException ex) {
            AlertUtils.showException("Network Error", "Could not contact backend.", ex);
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        close(event);
    }

    private void close(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
