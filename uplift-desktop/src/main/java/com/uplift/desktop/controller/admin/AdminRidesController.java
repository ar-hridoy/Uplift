package com.uplift.desktop.controller.admin;

import com.uplift.desktop.util.AlertUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdminRidesController {

    @FXML private TextField txtSearch;
    @FXML private ComboBox<String> cmbStatus;
    @FXML private Label lblStatus;

    // Instant table
    @FXML private TableView<InstantRideRow> tblInstant;
    @FXML private TableColumn<InstantRideRow, String> colIId;
    @FXML private TableColumn<InstantRideRow, String> colIPassenger;
    @FXML private TableColumn<InstantRideRow, String> colIRider;
    @FXML private TableColumn<InstantRideRow, String> colIFrom;
    @FXML private TableColumn<InstantRideRow, String> colITo;
    @FXML private TableColumn<InstantRideRow, String> colIFare;
    @FXML private TableColumn<InstantRideRow, String> colIStatus;
    @FXML private TableColumn<InstantRideRow, String> colIDate;

    // Scheduled table
    @FXML private TableView<ScheduledRideRow> tblScheduled;
    @FXML private TableColumn<ScheduledRideRow, String> colSId;
    @FXML private TableColumn<ScheduledRideRow, String> colSPassenger;
    @FXML private TableColumn<ScheduledRideRow, String> colSRider;
    @FXML private TableColumn<ScheduledRideRow, String> colSFrom;
    @FXML private TableColumn<ScheduledRideRow, String> colSTo;
    @FXML private TableColumn<ScheduledRideRow, String> colSSeats;
    @FXML private TableColumn<ScheduledRideRow, String> colSFare;
    @FXML private TableColumn<ScheduledRideRow, String> colSStatus;
    @FXML private TableColumn<ScheduledRideRow, String> colSWhen;

    private final ObservableList<InstantRideRow> instantData = FXCollections.observableArrayList();
    private final ObservableList<ScheduledRideRow> scheduledData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        cmbStatus.setItems(FXCollections.observableArrayList(
                "ALL", "ONGOING", "COMPLETED", "CANCELLED", "UPCOMING"
        ));
        cmbStatus.getSelectionModel().select("ALL");

        // bind columns - instant
        colIId.setCellValueFactory(x -> x.getValue().id);
        colIPassenger.setCellValueFactory(x -> x.getValue().passenger);
        colIRider.setCellValueFactory(x -> x.getValue().rider);
        colIFrom.setCellValueFactory(x -> x.getValue().from);
        colITo.setCellValueFactory(x -> x.getValue().to);
        colIFare.setCellValueFactory(x -> x.getValue().fare);
        colIStatus.setCellValueFactory(x -> x.getValue().status);
        colIDate.setCellValueFactory(x -> x.getValue().date);

        // bind columns - scheduled
        colSId.setCellValueFactory(x -> x.getValue().id);
        colSPassenger.setCellValueFactory(x -> x.getValue().passenger);
        colSRider.setCellValueFactory(x -> x.getValue().rider);
        colSFrom.setCellValueFactory(x -> x.getValue().from);
        colSTo.setCellValueFactory(x -> x.getValue().to);
        colSSeats.setCellValueFactory(x -> x.getValue().seats);
        colSFare.setCellValueFactory(x -> x.getValue().fare);
        colSStatus.setCellValueFactory(x -> x.getValue().status);
        colSWhen.setCellValueFactory(x -> x.getValue().when);

        // dummy data
        instantData.setAll(
                new InstantRideRow("I-101", "Rahim", "Rider One", "Uttara", "Dhanmondi", "৳180", "COMPLETED", "2025-12-11 09:30"),
                new InstantRideRow("I-102", "Karim", "—", "Mirpur", "Gulshan", "৳220", "ONGOING", "2025-12-13 01:05")
        );
        scheduledData.setAll(
                new ScheduledRideRow("S-201", "Sadia", "Rider One", "Banani", "Motijheel", "2", "৳260", "UPCOMING", "2025-12-14 10:00"),
                new ScheduledRideRow("S-202", "Nayeem", "—", "Uttara", "Farmgate", "3", "৳200", "UPCOMING", "2025-12-15 08:30")
        );

        tblInstant.setItems(instantData);
        tblScheduled.setItems(scheduledData);
    }

    @FXML
    private void handleRefresh(ActionEvent e) {
        lblStatus.setText("Refreshed at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        // TODO later: load from backend
    }

    @FXML
    private void handleSearch(ActionEvent e) {
        lblStatus.setText("Search not wired yet (UI ready).");
        // TODO later: filter locally or call backend with query params
    }

    @FXML
    private void handleReset(ActionEvent e) {
        txtSearch.clear();
        cmbStatus.getSelectionModel().select("ALL");
        lblStatus.setText("");
    }

    // -------- Actions: Instant --------
    @FXML
    private void viewInstantDetails(ActionEvent e) {
        InstantRideRow r = tblInstant.getSelectionModel().getSelectedItem();
        if (r == null) { AlertUtils.showError("No selection", "Select an instant ride first."); return; }
        AlertUtils.showInfo("Instant Ride Details",
                "Ride: " + r.id.get() + "\nPassenger: " + r.passenger.get() +
                        "\nRider: " + r.rider.get() + "\nFrom: " + r.from.get() +
                        "\nTo: " + r.to.get() + "\nStatus: " + r.status.get());
    }

    @FXML
    private void assignDriverInstant(ActionEvent e) {
        InstantRideRow r = tblInstant.getSelectionModel().getSelectedItem();
        if (r == null) { AlertUtils.showError("No selection", "Select an instant ride first."); return; }
        // TODO: open assign driver dialog + call backend
        AlertUtils.showInfo("Assign Driver", "TODO: Assign driver for " + r.id.get());
    }

    @FXML
    private void cancelInstant(ActionEvent e) {
        InstantRideRow r = tblInstant.getSelectionModel().getSelectedItem();
        if (r == null) { AlertUtils.showError("No selection", "Select an instant ride first."); return; }
        // TODO: call backend cancel
        r.status.set("CANCELLED");
        tblInstant.refresh();
    }

    // -------- Actions: Scheduled --------
    @FXML
    private void viewScheduledDetails(ActionEvent e) {
        ScheduledRideRow r = tblScheduled.getSelectionModel().getSelectedItem();
        if (r == null) { AlertUtils.showError("No selection", "Select a scheduled ride first."); return; }
        AlertUtils.showInfo("Scheduled Ride Details",
                "Ride: " + r.id.get() + "\nPassenger: " + r.passenger.get() +
                        "\nRider: " + r.rider.get() + "\nFrom: " + r.from.get() +
                        "\nTo: " + r.to.get() + "\nWhen: " + r.when.get() +
                        "\nStatus: " + r.status.get());
    }

    @FXML
    private void assignDriverScheduled(ActionEvent e) {
        ScheduledRideRow r = tblScheduled.getSelectionModel().getSelectedItem();
        if (r == null) { AlertUtils.showError("No selection", "Select a scheduled ride first."); return; }
        AlertUtils.showInfo("Assign Driver", "TODO: Assign driver for " + r.id.get());
    }

    @FXML
    private void modifyScheduled(ActionEvent e) {
        ScheduledRideRow r = tblScheduled.getSelectionModel().getSelectedItem();
        if (r == null) { AlertUtils.showError("No selection", "Select a scheduled ride first."); return; }
        AlertUtils.showInfo("Modify Ride", "TODO: Modify scheduled ride " + r.id.get());
    }

    @FXML
    private void cancelScheduled(ActionEvent e) {
        ScheduledRideRow r = tblScheduled.getSelectionModel().getSelectedItem();
        if (r == null) { AlertUtils.showError("No selection", "Select a scheduled ride first."); return; }
        r.status.set("CANCELLED");
        tblScheduled.refresh();
    }

    // -------- Row models --------
    public static class InstantRideRow {
        SimpleStringProperty id = new SimpleStringProperty();
        SimpleStringProperty passenger = new SimpleStringProperty();
        SimpleStringProperty rider = new SimpleStringProperty();
        SimpleStringProperty from = new SimpleStringProperty();
        SimpleStringProperty to = new SimpleStringProperty();
        SimpleStringProperty fare = new SimpleStringProperty();
        SimpleStringProperty status = new SimpleStringProperty();
        SimpleStringProperty date = new SimpleStringProperty();

        public InstantRideRow(String id, String passenger, String rider, String from, String to,
                              String fare, String status, String date) {
            this.id.set(id); this.passenger.set(passenger); this.rider.set(rider);
            this.from.set(from); this.to.set(to); this.fare.set(fare);
            this.status.set(status); this.date.set(date);
        }
    }

    public static class ScheduledRideRow {
        SimpleStringProperty id = new SimpleStringProperty();
        SimpleStringProperty passenger = new SimpleStringProperty();
        SimpleStringProperty rider = new SimpleStringProperty();
        SimpleStringProperty from = new SimpleStringProperty();
        SimpleStringProperty to = new SimpleStringProperty();
        SimpleStringProperty seats = new SimpleStringProperty();
        SimpleStringProperty fare = new SimpleStringProperty();
        SimpleStringProperty status = new SimpleStringProperty();
        SimpleStringProperty when = new SimpleStringProperty();

        public ScheduledRideRow(String id, String passenger, String rider, String from, String to,
                                String seats, String fare, String status, String when) {
            this.id.set(id); this.passenger.set(passenger); this.rider.set(rider);
            this.from.set(from); this.to.set(to); this.seats.set(seats);
            this.fare.set(fare); this.status.set(status); this.when.set(when);
        }
    }
}
