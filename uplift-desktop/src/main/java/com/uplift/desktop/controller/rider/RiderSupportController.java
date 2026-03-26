package com.uplift.desktop.controller.rider;

import com.uplift.desktop.util.AlertUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RiderSupportController {

    @FXML private TextField txtSearch;
    @FXML private ComboBox<String> cmbStatus;

    @FXML private TableView<Row> tblTickets;
    @FXML private TableColumn<Row, String> colId, colType, colStatus, colDate;

    @FXML private ComboBox<String> cmbType;
    @FXML private TextField txtRideId;
    @FXML private TextArea txtMessage;

    @FXML private Label lblSelectedMeta;
    @FXML private TextArea txtSelectedDetails;

    private final ObservableList<Row> data = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        cmbStatus.setItems(FXCollections.observableArrayList("ALL", "OPEN", "IN_PROGRESS", "RESOLVED"));
        cmbStatus.getSelectionModel().select("ALL");

        cmbType.setItems(FXCollections.observableArrayList("Payment", "Ride Issue", "Passenger Behavior", "App Bug", "Other"));

        colId.setCellValueFactory(x -> x.getValue().id);
        colType.setCellValueFactory(x -> x.getValue().type);
        colStatus.setCellValueFactory(x -> x.getValue().status);
        colDate.setCellValueFactory(x -> x.getValue().date);

        data.addAll(
                new Row("T-3001", "Passenger Behavior", "OPEN", "2025-12-12 21:05", "Passenger was rude during ride I-112."),
                new Row("T-3002", "Payment", "IN_PROGRESS", "2025-12-11 10:40", "Payout delayed for last week."),
                new Row("T-3003", "App Bug", "RESOLVED", "2025-12-10 18:12", "App crashed when opening Scheduled Rides.")
        );

        tblTickets.setItems(data);

        tblTickets.getSelectionModel().selectedItemProperty().addListener((obs, oldV, r) -> {
            if (r == null) return;
            lblSelectedMeta.setText(r.id.get() + " • " + r.type.get() + " • " + r.status.get());
            txtSelectedDetails.setText(r.details);
        });
    }

    @FXML
    private void handleApply(ActionEvent e) {
        AlertUtils.showInfo("Filters", "Filters applied (dummy).");
    }

    @FXML
    private void handleReset(ActionEvent e) {
        cmbType.getSelectionModel().clearSelection();
        txtRideId.clear();
        txtMessage.clear();
    }

    @FXML
    private void handleSubmit(ActionEvent e) {
        String type = cmbType.getValue();
        String msg = txtMessage.getText().trim();

        if (type == null || type.isBlank()) {
            AlertUtils.showError("Validation", "Please select a ticket type.");
            return;
        }
        if (msg.isEmpty()) {
            AlertUtils.showError("Validation", "Please write a message.");
            return;
        }

        // Dummy add
        data.add(0, new Row("T-" + (3000 + data.size() + 1), type, "OPEN", "Now", msg));
        tblTickets.refresh();

        AlertUtils.showInfo("Submitted", "Ticket created (dummy).");
        handleReset(e);
    }

    public static class Row {
        SimpleStringProperty id = new SimpleStringProperty();
        SimpleStringProperty type = new SimpleStringProperty();
        SimpleStringProperty status = new SimpleStringProperty();
        SimpleStringProperty date = new SimpleStringProperty();
        String details;

        public Row(String id, String type, String status, String date, String details) {
            this.id.set(id);
            this.type.set(type);
            this.status.set(status);
            this.date.set(date);
            this.details = details;
        }
    }
}
