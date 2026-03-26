package com.uplift.desktop.controller.passenger;

import com.uplift.desktop.dto.LoginResponse;
import com.uplift.desktop.util.AlertUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import com.uplift.desktop.controller.passenger.PassengerProfileController;

public class PassengerDashboardController {

    // top bar
    @FXML private Label lblPassengerName;
    @FXML private Label lblPassengerEmail;
    @FXML private Label lblCurrentDateTime;
    @FXML private Label lblCurrentDateDetail;

    // sidebar buttons
    @FXML private ToggleButton btnMenuDashboard;
    @FXML private ToggleButton btnMenuRequestRide;
    @FXML private ToggleButton btnMenuFindRide;
    @FXML private ToggleButton btnMenuPayments;
    @FXML private ToggleButton btnMenuComplaints;

    // center content
    @FXML private StackPane contentRoot;
    @FXML private AnchorPane dashboardContainer;

    // dashboard summary
    @FXML private Label lblTodayRides;
    @FXML private Label lblTodaySpending;
    @FXML private Label lblPendingRequests;
    @FXML private Label lblUpcomingRides;

    // recent rides table
    @FXML private TableView<RecentRideRow> tblRecentRides;
    @FXML private TableColumn<RecentRideRow, String> colId;
    @FXML private TableColumn<RecentRideRow, String> colType;
    @FXML private TableColumn<RecentRideRow, String> colFrom;
    @FXML private TableColumn<RecentRideRow, String> colTo;
    @FXML private TableColumn<RecentRideRow, String> colFare;
    @FXML private TableColumn<RecentRideRow, String> colStatus;
    @FXML private TableColumn<RecentRideRow, String> colDate;

    private ToggleGroup menuGroup;
    private LoginResponse session;

    // called from LoginController
    public void init(LoginResponse resp) {
        this.session = resp;
        lblPassengerName.setText("Welcome, " + resp.getFullName());
        lblPassengerEmail.setText(resp.getEmail());
    }

    @FXML
    private void initialize() {
        // group menu buttons
        menuGroup = new ToggleGroup();
        btnMenuDashboard.setToggleGroup(menuGroup);
        btnMenuRequestRide.setToggleGroup(menuGroup);
        btnMenuFindRide.setToggleGroup(menuGroup);
        btnMenuPayments.setToggleGroup(menuGroup);
        btnMenuComplaints.setToggleGroup(menuGroup);
        btnMenuDashboard.setSelected(true);

        updateDateTime();

        // table bindings
        colId.setCellValueFactory(d -> d.getValue().idProperty());
        colType.setCellValueFactory(d -> d.getValue().typeProperty());
        colFrom.setCellValueFactory(d -> d.getValue().fromProperty());
        colTo.setCellValueFactory(d -> d.getValue().toProperty());
        colFare.setCellValueFactory(d -> d.getValue().fareProperty());
        colStatus.setCellValueFactory(d -> d.getValue().statusProperty());
        colDate.setCellValueFactory(d -> d.getValue().dateProperty());

        // sample summary + recent rides (replace later with backend)
        lblTodayRides.setText("2");
        lblTodaySpending.setText("৳320");
        lblPendingRequests.setText("1");
        lblUpcomingRides.setText("1");

        tblRecentRides.setItems(getSampleRecentRides());
    }

    private void updateDateTime() {
        LocalDateTime now = LocalDateTime.now();
        lblCurrentDateTime.setText(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a")));
        lblCurrentDateDetail.setText(now.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
    }

    /* ===== Menu Handlers ===== */

    @FXML
    private void showDashboard(ActionEvent e) {
        selectMenu(btnMenuDashboard);
        setContent(dashboardContainer);
    }

    @FXML
    private void showRequestRide(ActionEvent e) {
        selectMenu(btnMenuRequestRide);
        loadAndSetContent("/fxml/passenger/request_ride.fxml");
    }

    @FXML
    private void showFindRide(ActionEvent e) {
        selectMenu(btnMenuFindRide);
        loadAndSetContent("/fxml/passenger/find_ride.fxml");
    }

    @FXML
    private void showPayments(ActionEvent e) {
        selectMenu(btnMenuPayments);
        loadAndSetContent("/fxml/passenger/payments.fxml");
    }

    @FXML
    private void showComplaints(ActionEvent e) {
        selectMenu(btnMenuComplaints);
        loadAndSetContent("/fxml/passenger/complaints.fxml");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/login.fxml"));
            BorderPane loginRoot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.centerOnScreen();
        } catch (IOException e) {
            AlertUtils.showException("Logout Error",
                    "Unable to go back to login screen.", e);
        }
    }

    @FXML
    private void openProfile(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/passenger/passenger_profile.fxml")
            );
            Node view = loader.load();

            PassengerProfileController controller = loader.getController();
            controller.init(session);

            contentRoot.getChildren().setAll(view);

        } catch (IOException e) {
            AlertUtils.showException("Profile Error", "Cannot open profile screen.", e);
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
            AlertUtils.showException("View Error",
                    "Cannot load screen: " + fxmlPath, e);
        }
    }

    /* ===== Recent Rides model + sample data ===== */

    public static class RecentRideRow {
        private final SimpleStringProperty id     = new SimpleStringProperty();
        private final SimpleStringProperty type   = new SimpleStringProperty();
        private final SimpleStringProperty from   = new SimpleStringProperty();
        private final SimpleStringProperty to     = new SimpleStringProperty();
        private final SimpleStringProperty fare   = new SimpleStringProperty();
        private final SimpleStringProperty status = new SimpleStringProperty();
        private final SimpleStringProperty date   = new SimpleStringProperty();

        public RecentRideRow(String id, String type, String from, String to,
                             String fare, String status, String date) {
            this.id.set(id);
            this.type.set(type);
            this.from.set(from);
            this.to.set(to);
            this.fare.set(fare);
            this.status.set(status);
            this.date.set(date);
        }

        public String getId()     { return id.get(); }
        public String getType()   { return type.get(); }
        public String getFrom()   { return from.get(); }
        public String getTo()     { return to.get(); }
        public String getFare()   { return fare.get(); }
        public String getStatus() { return status.get(); }
        public String getDate()   { return date.get(); }

        public SimpleStringProperty idProperty()     { return id; }
        public SimpleStringProperty typeProperty()   { return type; }
        public SimpleStringProperty fromProperty()   { return from; }
        public SimpleStringProperty toProperty()     { return to; }
        public SimpleStringProperty fareProperty()   { return fare; }
        public SimpleStringProperty statusProperty() { return status; }
        public SimpleStringProperty dateProperty()   { return date; }
    }

    private ObservableList<RecentRideRow> getSampleRecentRides() {
        return FXCollections.observableArrayList(
                new RecentRideRow("P-201", "Instant",   "Uttara",      "Dhanmondi", "৳180", "Completed", "2025-12-11 09:30"),
                new RecentRideRow("P-202", "Future",    "Mirpur",      "Banani",    "৳220", "Upcoming",  "2025-12-12 14:00"),
                new RecentRideRow("P-203", "Instant",   "Bashundhara", "Motijheel", "৳260", "Cancelled", "2025-12-10 18:15")
        );
    }
}
