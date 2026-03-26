package com.uplift.desktop.controller.rider;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RiderInstantRideController {

    @FXML private TextField txtSearch;
    @FXML private ComboBox<String> cbStatusFilter;

    @FXML private Label lblActiveCount;
    @FXML private Label lblPending;
    @FXML private Label lblAcceptedToday;
    @FXML private Label lblAvgPickupTime;

    @FXML private TableView<?> tblInstantRides;
    @FXML private TableColumn<?, ?> colId;
    @FXML private TableColumn<?, ?> colPassenger;
    @FXML private TableColumn<?, ?> colPickup;
    @FXML private TableColumn<?, ?> colDropoff;
    @FXML private TableColumn<?, ?> colDistance;
    @FXML private TableColumn<?, ?> colFare;
    @FXML private TableColumn<?, ?> colRequestedAt;
    @FXML private TableColumn<?, ?> colStatus;

    @FXML private Button btnRefresh;
    @FXML private Button btnViewDetails;
    @FXML private Button btnAcceptRide;

    @FXML
    private void initialize() {
        cbStatusFilter.getSelectionModel().selectFirst();
        // TODO: setup columns + load data from backend
    }

    @FXML
    private void handleRefresh() {
        // TODO: reload instant rides
    }

    @FXML
    private void handleViewDetails() {
        // TODO: open popup
    }

    @FXML
    private void handleAcceptRide() {
        // TODO: accept selected ride
    }
}
