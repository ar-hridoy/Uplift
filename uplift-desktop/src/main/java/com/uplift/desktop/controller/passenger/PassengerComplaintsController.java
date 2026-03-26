package com.uplift.desktop.controller.passenger;

import com.uplift.desktop.util.AlertUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class PassengerComplaintsController {

    @FXML private TextField txtSearch;
    @FXML private ComboBox<String> cmbStatus;

    @FXML private TableView<Row> tblComplaints;
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

        cmbType.setItems(FXCollections.observableArrayList(
                "Driver Behavior", "Overcharge / Fare", "Late Pickup", "Safety", "App Bug", "Other"
        ));

        colId.setCellValueFactory(x -> x.getValue().id);
        colType.setCellValueFactory(x -> x.getValue().type);
        colStatus.setCellValueFactory(x -> x.getValue().status);
        colDate.setCellValueFactory(x -> x.getValue().date);

        data.addAll(
                new Row("C-2101", "Overcharge / Fare", "OPEN", "2025-12-11 10:20", "Fare was higher than shown."),
                new Row("C-2102", "Late Pickup", "IN_PROGRESS", "2025-12-10 19:40", "Rider arrived 25 minutes late."),
                new Row("C-2103", "Safety", "RESOLVED", "2025-12-09 14:05", "Unsafe driving complaint.")
        );

        tblComplaints.setItems(data);

        tblComplaints.getSelectionModel().selectedItemProperty().addListener((obs, oldV, r) -> {
            if (r == null) return;
            lblSelectedMeta.setText(r.id.get() + " • " + r.type.get() + " • " + r.status.get());
            txtSelectedDetails.setText(r.details);
        });
    }

    @FXML
    private void handleApply(ActionEvent e) {
        AlertUtils.showInfo("Filter", "Filter applied (dummy).");
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
            AlertUtils.showError("Validation", "Please select a complaint type.");
            return;
        }
        if (msg.isEmpty()) {
            AlertUtils.showError("Validation", "Please write your complaint message.");
            return;
        }

        data.add(0, new Row("C-" + (2100 + data.size() + 1), type, "OPEN", "Now", msg));
        tblComplaints.refresh();

        AlertUtils.showInfo("Submitted", "Complaint submitted (dummy).");
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
