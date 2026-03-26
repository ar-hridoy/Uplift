package com.uplift.desktop.controller.rider;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uplift.desktop.api.ApiClient;
import com.uplift.desktop.api.TokenManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class RiderScheduledRideController {

    @FXML private DatePicker dpFrom;
    @FXML private DatePicker dpTo;
    @FXML private ComboBox<String> cbStatus;

    @FXML private Label lblUpcomingCount;
    @FXML private Label lblSeatsBooked;
    @FXML private Label lblTodayScheduled;

    @FXML private TableView<RiderScheduledRideRow> tblScheduledRides;
    @FXML private TableColumn<RiderScheduledRideRow, Long> colId;
    @FXML private TableColumn<RiderScheduledRideRow, String> colRoute;
    @FXML private TableColumn<RiderScheduledRideRow, String> colDateTime;
    @FXML private TableColumn<RiderScheduledRideRow, Integer> colSeats;
    @FXML private TableColumn<RiderScheduledRideRow, String> colPricePerSeat;
    @FXML private TableColumn<RiderScheduledRideRow, String> colStatus;
    @FXML private TableColumn<RiderScheduledRideRow, Void> colActions;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @FXML
    private void initialize() {

        dpFrom.setValue(LocalDate.now());
        dpTo.setValue(LocalDate.now().plusDays(7));

        cbStatus.setItems(FXCollections.observableArrayList(
                "All", "OPEN", "BOOKED", "COMPLETED", "CANCELLED"
        ));
        cbStatus.getSelectionModel().select("All");

        // Table bindings
        colId.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        colRoute.setCellValueFactory(data -> data.getValue().routeProperty());
        colDateTime.setCellValueFactory(data -> data.getValue().dateTimeProperty());
        colSeats.setCellValueFactory(data -> data.getValue().seatsLeftProperty().asObject());
        colPricePerSeat.setCellValueFactory(data -> data.getValue().pricePerSeatProperty());
        colStatus.setCellValueFactory(data -> data.getValue().statusProperty());

        loadScheduledRides();
        loadSummary();
    }

    @FXML
    private void handleRefresh() {
        loadScheduledRides();
        loadSummary();
    }
    // NEW fields for posting ride
    @FXML private TextField txtFrom;
    @FXML private TextField txtTo;
    @FXML private DatePicker dpRideDate;
    @FXML private TextField txtRideTime;
    @FXML private TextField txtTotalSeats;
    @FXML private TextField txtPricePerSeat;
    @FXML
    private void handleCreateScheduledRide() {
        try {
            String from = txtFrom.getText();
            String to = txtTo.getText();
            var date = dpRideDate.getValue();
            String timeStr = txtRideTime.getText();
            String seatsStr = txtTotalSeats.getText();
            String priceStr = txtPricePerSeat.getText();

            if (from == null || from.isBlank()
                    || to == null || to.isBlank()
                    || date == null
                    || timeStr == null || timeStr.isBlank()
                    || seatsStr == null || seatsStr.isBlank()
                    || priceStr == null || priceStr.isBlank()) {

                showAlert(Alert.AlertType.WARNING, "Validation",
                        "Please fill all fields.");
                return;
            }

            // Parse date & time into ISO string e.g. 2025-11-24T14:30:00
            var localTime = java.time.LocalTime.parse(timeStr); // expects HH:mm
            var dateTime = date.atTime(localTime);

            int totalSeats = Integer.parseInt(seatsStr.trim());
            double price = Double.parseDouble(priceStr.trim());

            // Build JSON matching ScheduledRideCreateDto
            var node = objectMapper.createObjectNode();
            node.put("fromLocation", from);
            node.put("toLocation", to);
            node.put("dateTime", dateTime.toString()); // ISO-8601 string
            node.put("totalSeats", totalSeats);
            node.put("pricePerSeat", price);

            String jsonBody = objectMapper.writeValueAsString(node);

            String token = TokenManager.getToken();
            String url = "http://localhost:8080/api/rider/scheduled/create";

            // use HttpUtils.post via ApiClient
            String response = ApiClient.post("/api/rider/scheduled/create", jsonBody, token);

            // or, if you want to centralize, add a method in ApiClient

            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Scheduled ride posted successfully!");

            // refresh table
            loadScheduledRides();
            loadSummary();

            // clear form
            txtFrom.clear();
            txtTo.clear();
            dpRideDate.setValue(null);
            txtRideTime.clear();
            txtTotalSeats.clear();
            txtPricePerSeat.clear();

        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Failed to post ride: " + ex.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }


    @FXML
    private void handleResetFilters() {
        dpFrom.setValue(LocalDate.now());
        dpTo.setValue(LocalDate.now().plusDays(7));
        cbStatus.getSelectionModel().select("All");

        loadScheduledRides();
        loadSummary();
    }

    private void loadScheduledRides() {
        try {
            String token = TokenManager.getToken();
            String json = ApiClient.getRelative("/api/rider/scheduled-rides", token);

            List<RiderScheduledRideRow> rows =
                    objectMapper.readValue(json, new TypeReference<>() {});

            tblScheduledRides.setItems(FXCollections.observableArrayList(rows));
            lblUpcomingCount.setText(String.valueOf(rows.size()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSummary() {
        try {
            String token = TokenManager.getToken();
            if (token == null || token.isBlank()) {
                System.out.println("⚠ Token is null/empty in loadSummary()");
                return;
            }

            // ✅ Use getRelative here
            String json = ApiClient.getRelative("/api/rider/dashboard-summary", token);

            var node = objectMapper.readTree(json);

            int upcoming = node.path("upcomingScheduled").asInt(0);
            int todayScheduled = node.path("todayScheduled").asInt(0); // or whatever key you use
            int seatsBooked = node.path("seatsBooked").asInt(0);

            lblUpcomingCount.setText(String.valueOf(upcoming));
            lblTodayScheduled.setText(String.valueOf(todayScheduled));
            lblSeatsBooked.setText(String.valueOf(seatsBooked));

        } catch (IOException e) {
            e.printStackTrace();
            // (optional) show an alert instead of just stack trace
        } catch (RuntimeException e) {
            // This catches the "HTTP 403 - ..." RuntimeException from HttpUtils
            e.printStackTrace();
            System.out.println("Error calling /api/rider/dashboard-summary: " + e.getMessage());
        }
    }

}
