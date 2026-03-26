package com.uplift.desktop.controller.rider;

import com.uplift.desktop.controller.passenger.PassengerProfileController;
import com.uplift.desktop.dto.LoginResponse;
import com.uplift.desktop.util.AlertUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RiderDashboardController {

    @FXML
    private ToggleButton btnMenuDashboard;

    @FXML
    private Label lblCurrentDateDetail;

    @FXML
    private ToggleButton btnMenuInstant;

    @FXML
    private ToggleButton btnMenuScheduled;

    @FXML
    private ToggleButton btnMenuEarnings;

    @FXML
    private ToggleButton btnMenuSupport;

    @FXML
    private StackPane contentRoot;

    @FXML
    private AnchorPane dashboardContainer;

    @FXML
    private Label lblCurrentDateTime;

    @FXML
    private Label lblRiderName;

    @FXML
    private Label lblRiderEmail;

    private ToggleGroup menuGroup;


    @FXML
    private void initialize() {
        // Menu toggle group
        menuGroup = new ToggleGroup();
        btnMenuDashboard.setToggleGroup(menuGroup);
        btnMenuInstant.setToggleGroup(menuGroup);
        btnMenuScheduled.setToggleGroup(menuGroup);
        btnMenuEarnings.setToggleGroup(menuGroup);
        btnMenuSupport.setToggleGroup(menuGroup);

        btnMenuDashboard.setSelected(true);

        if (session == null) {
            lblRiderName.setText("Welcome, Rider");
            lblRiderEmail.setText("");
        }


        updateDateTime();

        // dashboardContainer is already inside contentRoot by default
    }


   
    private void updateDateTime() {
        LocalDateTime now = LocalDateTime.now();

        // main line – date + time
        String main = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a"));
        lblCurrentDateTime.setText(main);

        // second line – long date (you can customize format)
        String detail = now.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy"));
        lblCurrentDateDetail.setText(detail);
    }


    /* ===== Menu Actions ===== */

    @FXML
    private void showDashboard(ActionEvent event) {
        selectMenu(btnMenuDashboard);
        setContent(dashboardContainer);
    }
    @FXML
    private void showScheduledRides() {
        loadAndSetContent("/fxml/rider/rider_scheduled.fxml");
    }

    @FXML
    private void showInstantRides(ActionEvent event) {
        selectMenu(btnMenuInstant);
        loadAndSetContent("/fxml/rider/rider_instant.fxml");
    }

    @FXML
    private void showScheduledRides(ActionEvent event) {
        selectMenu(btnMenuScheduled);
        loadAndSetContent("/fxml/rider/rider_scheduled.fxml");
    }

    @FXML
    private void showEarnings(ActionEvent event) {
        selectMenu(btnMenuEarnings);
        loadAndSetContent("/fxml/rider/rider_earnings.fxml");
    }

    @FXML
    private void showSupport(ActionEvent event) {
        selectMenu(btnMenuSupport);
        loadAndSetContent("/fxml/rider/rider_support.fxml");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/login.fxml")
            );
            BorderPane root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("UpLift - Login");
            stage.centerOnScreen();

            // optional: clear token
            // TokenManager.clear();

        } catch (IOException e) {
            AlertUtils.showException(
                    "Logout Error",
                    "Unable to return to login screen.",
                    e
            );
        }
    }


    @FXML
    private void openProfile(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/rider/rider_profile.fxml")
            );
            Node view = loader.load();

            RiderProfileController controller = loader.getController();
            controller.init(session);

            contentRoot.getChildren().setAll(view);

        } catch (IOException e) {
            AlertUtils.showException("Profile Error", "Cannot open profile screen.", e);
        }
    }


    private LoginResponse session;

    public void init(LoginResponse resp) {
        this.session = resp;

        // Update UI labels
        if (lblRiderName != null) {
            lblRiderName.setText("Welcome, " + resp.getFullName());
        }
        if (lblRiderEmail != null) {
            lblRiderEmail.setText(resp.getEmail());
        }
    }



    /* ===== Helpers ===== */

    private void selectMenu(ToggleButton button) {
        menuGroup.selectToggle(button);
    }

    private void setContent(Node node) {
        contentRoot.getChildren().setAll(node);
    }

    private void loadAndSetContent(String fxmlPath) {
        try {
            Node view = FXMLLoader.load(getClass().getResource(fxmlPath));
            setContent(view);
        } catch (IOException e) {
            e.printStackTrace();
            // in real app: show error dialog
        }
    }
}
