package com.uplift.desktop.controller.admin;

import com.uplift.desktop.util.AlertUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdminVehiclesController {

    @FXML private TextField txtSearch;
    @FXML private ComboBox<String> cmbStatus;
    @FXML private Label lblStatus;

    // Pending table
    @FXML private TableView<PendingVehicleRow> tblPending;
    @FXML private TableColumn<PendingVehicleRow, String> colPId;
    @FXML private TableColumn<PendingVehicleRow, String> colPRider;
    @FXML private TableColumn<PendingVehicleRow, String> colPType;
    @FXML private TableColumn<PendingVehicleRow, String> colPPlate;
    @FXML private TableColumn<PendingVehicleRow, String> colPModel;
    @FXML private TableColumn<PendingVehicleRow, String> colPSubmitted;
    @FXML private TableColumn<PendingVehicleRow, String> colPStatus;

    // All table
    @FXML private TableView<VehicleRow> tblAll;
    @FXML private TableColumn<VehicleRow, String> colAId;
    @FXML private TableColumn<VehicleRow, String> colARider;
    @FXML private TableColumn<VehicleRow, String> colAType;
    @FXML private TableColumn<VehicleRow, String> colAPlate;
    @FXML private TableColumn<VehicleRow, String> colAModel;
    @FXML private TableColumn<VehicleRow, String> colAActive;
    @FXML private TableColumn<VehicleRow, String> colAStatus;
    @FXML private TableColumn<VehicleRow, String> colAUpdated;

    private final ObservableList<PendingVehicleRow> pendingData = FXCollections.observableArrayList();
    private final ObservableList<VehicleRow> allData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        cmbStatus.setItems(FXCollections.observableArrayList("ALL", "PENDING", "APPROVED", "REJECTED"));
        cmbStatus.getSelectionModel().select("ALL");

        // bind pending columns
        colPId.setCellValueFactory(x -> x.getValue().id);
        colPRider.setCellValueFactory(x -> x.getValue().rider);
        colPType.setCellValueFactory(x -> x.getValue().type);
        colPPlate.setCellValueFactory(x -> x.getValue().plate);
        colPModel.setCellValueFactory(x -> x.getValue().model);
        colPSubmitted.setCellValueFactory(x -> x.getValue().submitted);
        colPStatus.setCellValueFactory(x -> x.getValue().status);

        // bind all columns
        colAId.setCellValueFactory(x -> x.getValue().id);
        colARider.setCellValueFactory(x -> x.getValue().rider);
        colAType.setCellValueFactory(x -> x.getValue().type);
        colAPlate.setCellValueFactory(x -> x.getValue().plate);
        colAModel.setCellValueFactory(x -> x.getValue().model);
        colAActive.setCellValueFactory(x -> x.getValue().active);
        colAStatus.setCellValueFactory(x -> x.getValue().verifyStatus);
        colAUpdated.setCellValueFactory(x -> x.getValue().updated);

        // dummy data
        pendingData.setAll(
                new PendingVehicleRow("V-301", "Rider One (rider1@uplift.com)", "Bike", "DHK-1234", "Yamaha FZ", "2025-12-12 18:20", "PENDING"),
                new PendingVehicleRow("V-302", "Demo Rider (rider@uplift.com)", "Car", "DHK-7788", "Toyota Axio", "2025-12-13 00:10", "PENDING")
        );

        allData.setAll(
                new VehicleRow("V-101", "Rider One", "Bike", "DHK-1111", "Honda", "YES", "APPROVED", "2025-12-10 09:10"),
                new VehicleRow("V-102", "Rider Two", "Car", "DHK-2222", "Corolla", "NO", "REJECTED", "2025-12-11 11:45"),
                new VehicleRow("V-103", "Demo Rider", "Car", "DHK-3333", "Premio", "YES", "APPROVED", "2025-12-12 07:30")
        );

        tblPending.setItems(pendingData);
        tblAll.setItems(allData);
    }

    @FXML
    private void handleRefresh(ActionEvent e) {
        lblStatus.setText("Refreshed at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        // TODO: call backend
    }

    @FXML
    private void handleSearch(ActionEvent e) {
        lblStatus.setText("Search not wired yet (UI ready).");
        // TODO: filter locally / backend
    }

    @FXML
    private void handleReset(ActionEvent e) {
        txtSearch.clear();
        cmbStatus.getSelectionModel().select("ALL");
        lblStatus.setText("");
    }

    // ----- Pending actions -----
    @FXML
    private void viewPendingDocs(ActionEvent e) {
        PendingVehicleRow r = tblPending.getSelectionModel().getSelectedItem();
        if (r == null) { AlertUtils.showError("No selection", "Select a pending vehicle first."); return; }

        openDocsModal(
                r.id.get(),
                r.rider.get(),
                java.util.List.of("license.jpg", "registration.pdf", "nid_front.jpg")
        );
    }

    @FXML
    private void approvePending(ActionEvent e) {
        PendingVehicleRow r = tblPending.getSelectionModel().getSelectedItem();
        if (r == null) { AlertUtils.showError("No selection", "Select a pending vehicle first."); return; }
        r.status.set("APPROVED");
        tblPending.refresh();
        AlertUtils.showInfo("Approved", "Vehicle " + r.id.get() + " approved.");
        // TODO: backend approve
    }

    @FXML
    private void rejectPending(ActionEvent e) {
        PendingVehicleRow r = tblPending.getSelectionModel().getSelectedItem();
        if (r == null) { AlertUtils.showError("No selection", "Select a pending vehicle first."); return; }
        r.status.set("REJECTED");
        tblPending.refresh();
        AlertUtils.showInfo("Rejected", "Vehicle " + r.id.get() + " rejected.");
        // TODO: backend reject
    }

    // ----- All actions -----
    @FXML
    private void viewAllDocs(ActionEvent e) {
        VehicleRow r = tblAll.getSelectionModel().getSelectedItem();
        if (r == null) { AlertUtils.showError("No selection", "Select a vehicle first."); return; }

        openDocsModal(
                r.id.get(),
                r.rider.get(),
                java.util.List.of("license.jpg", "registration.pdf")
        );
    }

    private void openDocsModal(String vehicleId, String rider, java.util.List<String> docs) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/admin_vehicle_docs.fxml"));
            Scene scene = new Scene(loader.load());

            AdminVehicleDocsController c = loader.getController();
            c.init(vehicleId, rider, docs);

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Vehicle Documents");
            dialog.setScene(scene);
            dialog.centerOnScreen();
            dialog.showAndWait();

        } catch (Exception ex) {
            ex.printStackTrace();
            AlertUtils.showError("View Docs Error", "Could not open documents window.");
        }
    }

    @FXML
    private void editVehicle(ActionEvent e) {
        VehicleRow r = tblAll.getSelectionModel().getSelectedItem();
        if (r == null) { AlertUtils.showError("No selection", "Select a vehicle first."); return; }
        AlertUtils.showInfo("Edit Vehicle", "TODO: edit " + r.id.get());
    }

    @FXML
    private void toggleActive(ActionEvent e) {
        VehicleRow r = tblAll.getSelectionModel().getSelectedItem();
        if (r == null) { AlertUtils.showError("No selection", "Select a vehicle first."); return; }

        boolean isActive = "YES".equalsIgnoreCase(r.active.get());
        r.active.set(isActive ? "NO" : "YES");
        tblAll.refresh();

        // TODO: backend toggle active
    }

    // ------- Row models -------
    public static class PendingVehicleRow {
        SimpleStringProperty id = new SimpleStringProperty();
        SimpleStringProperty rider = new SimpleStringProperty();
        SimpleStringProperty type = new SimpleStringProperty();
        SimpleStringProperty plate = new SimpleStringProperty();
        SimpleStringProperty model = new SimpleStringProperty();
        SimpleStringProperty submitted = new SimpleStringProperty();
        SimpleStringProperty status = new SimpleStringProperty();

        public PendingVehicleRow(String id, String rider, String type, String plate, String model, String submitted, String status) {
            this.id.set(id); this.rider.set(rider); this.type.set(type);
            this.plate.set(plate); this.model.set(model); this.submitted.set(submitted); this.status.set(status);
        }
    }

    public static class VehicleRow {
        SimpleStringProperty id = new SimpleStringProperty();
        SimpleStringProperty rider = new SimpleStringProperty();
        SimpleStringProperty type = new SimpleStringProperty();
        SimpleStringProperty plate = new SimpleStringProperty();
        SimpleStringProperty model = new SimpleStringProperty();
        SimpleStringProperty active = new SimpleStringProperty();
        SimpleStringProperty verifyStatus = new SimpleStringProperty();
        SimpleStringProperty updated = new SimpleStringProperty();

        public VehicleRow(String id, String rider, String type, String plate, String model, String active, String verifyStatus, String updated) {
            this.id.set(id); this.rider.set(rider); this.type.set(type);
            this.plate.set(plate); this.model.set(model); this.active.set(active);
            this.verifyStatus.set(verifyStatus); this.updated.set(updated);
        }
    }
}
