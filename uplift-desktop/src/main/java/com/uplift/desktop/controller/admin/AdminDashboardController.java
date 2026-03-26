package com.uplift.desktop.controller.admin;

import com.uplift.desktop.dto.LoginResponse;
import com.uplift.desktop.util.AlertUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdminDashboardController {

    // topbar
    @FXML private Label lblAdminName;
    @FXML private Label lblAdminEmail;
    @FXML private Label lblCurrentDateTime;
    @FXML private Label lblCurrentDateDetail;

    // sidebar
    @FXML private ToggleButton btnMenuDashboard;
    @FXML private ToggleButton btnMenuUsers;
    @FXML private ToggleButton btnMenuRides;
    @FXML private ToggleButton btnMenuVehicles;
    @FXML private ToggleButton btnMenuPayments;
    @FXML private ToggleButton btnMenuSystem;
    @FXML private ToggleButton btnMenuComplaints;

    // content
    @FXML private StackPane contentRoot;
    @FXML private AnchorPane dashboardContainer;

    // dashboard widgets
    @FXML private Label lblTotalRiders;
    @FXML private Label lblTotalPassengers;
    @FXML private Label lblRidesToday;
    @FXML private Label lblPendingApprovals;
    @FXML private TableView<?> tblActivity;

    private ToggleGroup menuGroup;
    private LoginResponse session;

    public void init(LoginResponse resp) {
        this.session = resp;

        if (resp != null) {
            lblAdminName.setText("Welcome, " + resp.getFullName());
            lblAdminEmail.setText(resp.getEmail());
        }
    }



    @FXML
    private void initialize() {
        menuGroup = new ToggleGroup();
        btnMenuDashboard.setToggleGroup(menuGroup);
        btnMenuUsers.setToggleGroup(menuGroup);
        btnMenuRides.setToggleGroup(menuGroup);
        btnMenuVehicles.setToggleGroup(menuGroup);
        btnMenuPayments.setToggleGroup(menuGroup);
        btnMenuSystem.setToggleGroup(menuGroup);
        btnMenuComplaints.setToggleGroup(menuGroup);

        btnMenuDashboard.setSelected(true);

        // demo display (later load from login/session)
        lblAdminName.setText("Welcome, Admin");
        lblAdminEmail.setText("admin@uplift.com");

        updateDateTime();

        // dummy values
        lblTotalRiders.setText("0");
        lblTotalPassengers.setText("0");
        lblRidesToday.setText("0");
        lblPendingApprovals.setText("0");
    }

    private void updateDateTime() {
        LocalDateTime now = LocalDateTime.now();
        lblCurrentDateTime.setText(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a")));
        lblCurrentDateDetail.setText(now.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
    }

    // Menu actions
    @FXML private void showDashboard(ActionEvent e) { select(btnMenuDashboard); setContent(dashboardContainer); }
    @FXML private void showUsers(ActionEvent e)     { select(btnMenuUsers); loadAndSet("/fxml/admin/admin_users.fxml"); }
    @FXML private void showRides(ActionEvent e)     { select(btnMenuRides); loadAndSet("/fxml/admin/admin_rides.fxml"); }
    @FXML private void showVehicles(ActionEvent e)  { select(btnMenuVehicles); loadAndSet("/fxml/admin/admin_vehicles.fxml"); }
    @FXML private void showPayments(ActionEvent e)  { select(btnMenuPayments); loadAndSet("/fxml/admin/admin_payments.fxml"); }
    @FXML private void showSystem(ActionEvent e)    { select(btnMenuSystem); loadAndSet("/fxml/admin/admin_system.fxml"); }
    @FXML private void showComplaints(ActionEvent e){ select(btnMenuComplaints); loadAndSet("/fxml/admin/admin_complaints.fxml"); }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            BorderPane loginRoot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.centerOnScreen();
        } catch (IOException ex) {
            AlertUtils.showException("Logout Error", "Unable to return to login.", ex);
        }
    }

    @FXML
    private void openProfile(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/admin/admin_profile.fxml")
            );
            Node view = loader.load();

            AdminProfileController controller = loader.getController();
            controller.init(session);

            contentRoot.getChildren().setAll(view);

        } catch (IOException e) {
            AlertUtils.showException("Profile Error", "Cannot open profile screen.", e);
        }
    }


    private void select(ToggleButton btn) {
        menuGroup.selectToggle(btn);
    }

    private void setContent(Node node) {
        contentRoot.getChildren().setAll(node);
    }

    private void loadAndSet(String fxmlPath) {
        try {
            Node view = FXMLLoader.load(getClass().getResource(fxmlPath));
            setContent(view);
        } catch (IOException ex) {
            AlertUtils.showException("View Error", "Cannot load: " + fxmlPath, ex);
        }
    }
}
