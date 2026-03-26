package com.uplift.desktop.controller.admin;

import com.uplift.desktop.util.AlertUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AdminComplaintsController {

    @FXML private TextField txtSearch;
    @FXML private ComboBox<String> cmbStatus;

    @FXML private TableView<Row> tblComplaints;
    @FXML private TableColumn<Row, String> colId, colUser, colRide, colType, colStatus, colDate;

    @FXML private Label lblMeta;
    @FXML private TextArea txtMessage;
    @FXML private TextArea txtResponse;

    private final ObservableList<Row> data = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        cmbStatus.setItems(FXCollections.observableArrayList("ALL", "OPEN", "IN_PROGRESS", "RESOLVED"));
        cmbStatus.getSelectionModel().select("ALL");

        colId.setCellValueFactory(x -> x.getValue().id);
        colUser.setCellValueFactory(x -> x.getValue().user);
        colRide.setCellValueFactory(x -> x.getValue().ride);
        colType.setCellValueFactory(x -> x.getValue().type);
        colStatus.setCellValueFactory(x -> x.getValue().status);
        colDate.setCellValueFactory(x -> x.getValue().date);

        data.addAll(
                new Row("C-401", "passenger2@uplift.com", "P-202", "Payment dispute", "OPEN", "2025-12-12 12:20", "Charged twice for ride P-202."),
                new Row("C-402", "rider1@uplift.com", "I-112", "Behavior", "IN_PROGRESS", "2025-12-12 13:05", "Passenger was abusive."),
                new Row("C-403", "passenger3@uplift.com", "P-201", "Safety", "RESOLVED", "2025-12-11 10:10", "Rider drove too fast.")
        );

        tblComplaints.setItems(data);

        tblComplaints.getSelectionModel().selectedItemProperty().addListener((obs, oldV, row) -> {
            if (row == null) return;
            lblMeta.setText(row.id.get() + " • " + row.user.get() + " • " + row.ride.get() + " • " + row.status.get());
            txtMessage.setText(row.message);
            txtResponse.clear();
        });
    }

    @FXML
    private void handleApply(ActionEvent e) {
        AlertUtils.showInfo("Filters", "Filters applied (dummy).");
    }

    @FXML
    private void handleInProgress(ActionEvent e) {
        Row r = tblComplaints.getSelectionModel().getSelectedItem();
        if (r == null) { AlertUtils.showError("No selection", "Select a complaint first."); return; }
        r.status.set("IN_PROGRESS");
        tblComplaints.refresh();
        AlertUtils.showInfo("Updated", "Marked as IN_PROGRESS (dummy).");
    }

    @FXML
    private void handleResolve(ActionEvent e) {
        Row r = tblComplaints.getSelectionModel().getSelectedItem();
        if (r == null) { AlertUtils.showError("No selection", "Select a complaint first."); return; }

        if (txtResponse.getText().trim().isEmpty()) {
            AlertUtils.showError("Response required", "Write a response before resolving.");
            return;
        }

        r.status.set("RESOLVED");
        tblComplaints.refresh();
        AlertUtils.showInfo("Resolved", "Complaint resolved (dummy).");
    }

    @FXML
    private void handleBlockUser(ActionEvent e) {
        Row r = tblComplaints.getSelectionModel().getSelectedItem();
        if (r == null) { AlertUtils.showError("No selection", "Select a complaint first."); return; }
        AlertUtils.showInfo("Block", "Pretend blocked user: " + r.user.get());
    }

    public static class Row {
        SimpleStringProperty id = new SimpleStringProperty();
        SimpleStringProperty user = new SimpleStringProperty();
        SimpleStringProperty ride = new SimpleStringProperty();
        SimpleStringProperty type = new SimpleStringProperty();
        SimpleStringProperty status = new SimpleStringProperty();
        SimpleStringProperty date = new SimpleStringProperty();
        String message;

        public Row(String id, String user, String ride, String type, String status, String date, String message) {
            this.id.set(id);
            this.user.set(user);
            this.ride.set(ride);
            this.type.set(type);
            this.status.set(status);
            this.date.set(date);
            this.message = message;
        }
    }
}
