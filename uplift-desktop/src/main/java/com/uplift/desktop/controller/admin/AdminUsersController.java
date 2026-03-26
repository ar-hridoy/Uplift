package com.uplift.desktop.controller.admin;

import com.uplift.desktop.util.AlertUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class AdminUsersController {

    // toolbar
    @FXML private TextField txtSearch;
    @FXML private ChoiceBox<String> cbRole;
    @FXML private ChoiceBox<String> cbStatus;
    @FXML private Label lblHint;

    // tables
    @FXML private TableView<UserRow> tblAllUsers;
    @FXML private TableColumn<UserRow, String> colId;
    @FXML private TableColumn<UserRow, String> colName;
    @FXML private TableColumn<UserRow, String> colUsername;
    @FXML private TableColumn<UserRow, String> colEmail;
    @FXML private TableColumn<UserRow, String> colRole;
    @FXML private TableColumn<UserRow, String> colStatus;
    @FXML private TableColumn<UserRow, String> colCreated;

    @FXML private TableView<UserRow> tblPendingRiders;
    @FXML private TableColumn<UserRow, String> colPId;
    @FXML private TableColumn<UserRow, String> colPName;
    @FXML private TableColumn<UserRow, String> colPEmail;
    @FXML private TableColumn<UserRow, String> colPPhone;
    @FXML private TableColumn<UserRow, String> colPStatus;
    @FXML private TableColumn<UserRow, String> colPRequested;

    @FXML private TableView<LogRow> tblLogs;
    @FXML private TableColumn<LogRow, String> colLTime;
    @FXML private TableColumn<LogRow, String> colLActor;
    @FXML private TableColumn<LogRow, String> colLAction;
    @FXML private TableColumn<LogRow, String> colLDetails;

    // in-memory demo data (replace with backend calls later)
    private ObservableList<UserRow> allUsers = FXCollections.observableArrayList();
    private ObservableList<UserRow> pendingRiders = FXCollections.observableArrayList();
    private ObservableList<LogRow> logs = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // dropdown options
        cbRole.setItems(FXCollections.observableArrayList("ALL", "PASSENGER", "RIDER", "ADMIN"));
        cbRole.setValue("ALL");

        cbStatus.setItems(FXCollections.observableArrayList("ALL", "ACTIVE", "INACTIVE", "PENDING", "REJECTED"));
        cbStatus.setValue("ALL");

        // bind columns (PropertyValueFactory needs getters with same names)
        colId.setCellValueFactory(v -> v.getValue().idProperty());
        colName.setCellValueFactory(v -> v.getValue().nameProperty());
        colUsername.setCellValueFactory(v -> v.getValue().usernameProperty());
        colEmail.setCellValueFactory(v -> v.getValue().emailProperty());
        colRole.setCellValueFactory(v -> v.getValue().roleProperty());
        colStatus.setCellValueFactory(v -> v.getValue().statusProperty());
        colCreated.setCellValueFactory(v -> v.getValue().createdProperty());

        colPId.setCellValueFactory(v -> v.getValue().idProperty());
        colPName.setCellValueFactory(v -> v.getValue().nameProperty());
        colPEmail.setCellValueFactory(v -> v.getValue().emailProperty());
        colPPhone.setCellValueFactory(v -> v.getValue().phoneProperty());
        colPStatus.setCellValueFactory(v -> v.getValue().statusProperty());
        colPRequested.setCellValueFactory(v -> v.getValue().createdProperty());

        colLTime.setCellValueFactory(v -> v.getValue().timeProperty());
        colLActor.setCellValueFactory(v -> v.getValue().actorProperty());
        colLAction.setCellValueFactory(v -> v.getValue().actionProperty());
        colLDetails.setCellValueFactory(v -> v.getValue().detailsProperty());

        // load demo data
        seedDemoData();

        tblAllUsers.setItems(allUsers);
        tblPendingRiders.setItems(pendingRiders);
        tblLogs.setItems(logs);
    }

    // ---------- Toolbar actions ----------

    @FXML
    private void handleApplyFilters(ActionEvent event) {
        String q = safeLower(txtSearch.getText());
        String role = cbRole.getValue();
        String status = cbStatus.getValue();

        ObservableList<UserRow> filtered = FXCollections.observableArrayList();

        for (UserRow u : allUsers) {
            boolean matchQ =
                    q.isBlank()
                            || safeLower(u.getName()).contains(q)
                            || safeLower(u.getEmail()).contains(q)
                            || safeLower(u.getUsername()).contains(q);

            boolean matchRole = "ALL".equals(role) || role.equalsIgnoreCase(u.getRole());
            boolean matchStatus = "ALL".equals(status) || status.equalsIgnoreCase(u.getStatus());

            if (matchQ && matchRole && matchStatus) filtered.add(u);
        }

        tblAllUsers.setItems(filtered);
        lblHint.setText("Filtered results: " + filtered.size());
    }

    @FXML
    private void handleCreateRider(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/admin_create_rider.fxml"));
            BorderPane root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Create Rider");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initOwner(((javafx.scene.Node) event.getSource()).getScene().getWindow());
            stage.setResizable(false);
            stage.showAndWait();

            // after closing, refresh (later: reload from backend)
            handleRefresh(event);

        } catch (IOException e) {
            AlertUtils.showException("UI Error", "Cannot open Create Rider window.", e);
        }
    }


    @FXML
    private void handleClearFilters(ActionEvent event) {
        txtSearch.clear();
        cbRole.setValue("ALL");
        cbStatus.setValue("ALL");
        tblAllUsers.setItems(allUsers);
        lblHint.setText("Tip: Select a row then use action buttons (Approve/Deactivate/Reset Password etc.)");
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        // later: call backend to reload tables
        tblAllUsers.setItems(allUsers);
        tblPendingRiders.setItems(pendingRiders);
        AlertUtils.showInfo("Refresh", "Demo refresh done (connect backend later).");
    }

    // ---------- Admin actions (demo) ----------

    @FXML
    private void handleApproveRider(ActionEvent event) {
        UserRow selected = getSelectedFromEitherTable();
        if (selected == null) return;

        if (!"RIDER".equalsIgnoreCase(selected.getRole())) {
            AlertUtils.showError("Invalid selection", "Please select a RIDER account.");
            return;
        }

        selected.setStatus("ACTIVE");
        pendingRiders.remove(selected);
        addLog("ADMIN", "APPROVE_RIDER", "Approved rider: " + selected.getEmail());
        tblAllUsers.refresh();
        tblPendingRiders.refresh();

        AlertUtils.showInfo("Approved", "Rider approved and activated.");
    }

    @FXML
    private void handleRejectRider(ActionEvent event) {
        UserRow selected = getSelectedFromEitherTable();
        if (selected == null) return;

        if (!"RIDER".equalsIgnoreCase(selected.getRole())) {
            AlertUtils.showError("Invalid selection", "Please select a RIDER account.");
            return;
        }

        selected.setStatus("REJECTED");
        pendingRiders.remove(selected);
        addLog("ADMIN", "REJECT_RIDER", "Rejected rider: " + selected.getEmail());
        tblAllUsers.refresh();
        tblPendingRiders.refresh();

        AlertUtils.showInfo("Rejected", "Rider rejected.");
    }

    @FXML
    private void handleActivateUser(ActionEvent event) {
        UserRow selected = getSelectedUser(tblAllUsers);
        if (selected == null) return;

        selected.setStatus("ACTIVE");
        addLog("ADMIN", "ACTIVATE_USER", "Activated user: " + selected.getEmail());
        tblAllUsers.refresh();
        AlertUtils.showInfo("Activated", "User is now ACTIVE.");
    }

    @FXML
    private void handleDeactivateUser(ActionEvent event) {
        UserRow selected = getSelectedUser(tblAllUsers);
        if (selected == null) return;

        selected.setStatus("INACTIVE");
        addLog("ADMIN", "DEACTIVATE_USER", "Deactivated user: " + selected.getEmail());
        tblAllUsers.refresh();
        AlertUtils.showInfo("Deactivated", "User is now INACTIVE.");
    }

    @FXML
    private void handleResetPassword(ActionEvent event) {
        UserRow selected = getSelectedUser(tblAllUsers);
        if (selected == null) return;

        // demo only — later open a dialog + call backend
        addLog("ADMIN", "RESET_PASSWORD", "Reset password for: " + selected.getEmail());
        AlertUtils.showInfo("Reset Password", "Demo: password reset request created (backend later).");
    }

    @FXML
    private void handleEditUser(ActionEvent event) {
        UserRow selected = getSelectedUser(tblAllUsers);
        if (selected == null) return;

        // demo only — later open edit form dialog
        addLog("ADMIN", "EDIT_USER", "Edit requested for: " + selected.getEmail());
        AlertUtils.showInfo("Edit User", "Demo: open edit dialog later for " + selected.getEmail());
    }

    @FXML
    private void handleReloadLogs(ActionEvent event) {
        // later: load logs from backend
        AlertUtils.showInfo("Logs", "Demo logs shown. Backend integration later.");
    }

    // ---------- helpers ----------

    private UserRow getSelectedFromEitherTable() {
        UserRow s1 = tblPendingRiders.getSelectionModel().getSelectedItem();
        if (s1 != null) return s1;

        UserRow s2 = tblAllUsers.getSelectionModel().getSelectedItem();
        if (s2 != null) return s2;

        AlertUtils.showError("No selection", "Please select a user first.");
        return null;
    }

    private UserRow getSelectedUser(TableView<UserRow> table) {
        UserRow selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showError("No selection", "Please select a user first.");
            return null;
        }
        return selected;
    }

    private void addLog(String actor, String action, String details) {
        logs.add(0, new LogRow(now(), actor, action, details));
        tblLogs.refresh();
    }

    private String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private String safeLower(String s) {
        return s == null ? "" : s.toLowerCase(Locale.ROOT).trim();
    }

    private void seedDemoData() {
        allUsers.clear();
        pendingRiders.clear();
        logs.clear();

        allUsers.addAll(
                new UserRow("1", "Admin User", "admin", "admin@uplift.com", "ADMIN", "ACTIVE", "0123", "2025-12-01"),
                new UserRow("2", "Passenger One", "passenger1", "passenger1@uplift.com", "PASSENGER", "ACTIVE", "0170", "2025-12-02"),
                new UserRow("3", "Rider Pending", "rider_pending", "rider.pending@uplift.com", "RIDER", "PENDING", "0180", "2025-12-10"),
                new UserRow("4", "Rider Active", "rider1", "rider1@uplift.com", "RIDER", "ACTIVE", "0190", "2025-12-05"),
                new UserRow("5", "Passenger Inactive", "passenger2", "passenger2@uplift.com", "PASSENGER", "INACTIVE", "0160", "2025-12-03")
        );

        // pending riders table shows subset
        for (UserRow u : allUsers) {
            if ("RIDER".equalsIgnoreCase(u.getRole()) && "PENDING".equalsIgnoreCase(u.getStatus())) {
                pendingRiders.add(u);
            }
        }

        logs.addAll(
                new LogRow(now(), "SYSTEM", "BOOT", "Admin module loaded"),
                new LogRow(now(), "ADMIN", "VIEW_USERS", "Opened User Management screen")
        );
    }

    // ---------- row models ----------

    public static class UserRow {
        private final javafx.beans.property.SimpleStringProperty id = new javafx.beans.property.SimpleStringProperty();
        private final javafx.beans.property.SimpleStringProperty name = new javafx.beans.property.SimpleStringProperty();
        private final javafx.beans.property.SimpleStringProperty username = new javafx.beans.property.SimpleStringProperty();
        private final javafx.beans.property.SimpleStringProperty email = new javafx.beans.property.SimpleStringProperty();
        private final javafx.beans.property.SimpleStringProperty role = new javafx.beans.property.SimpleStringProperty();
        private final javafx.beans.property.SimpleStringProperty status = new javafx.beans.property.SimpleStringProperty();
        private final javafx.beans.property.SimpleStringProperty phone = new javafx.beans.property.SimpleStringProperty();
        private final javafx.beans.property.SimpleStringProperty created = new javafx.beans.property.SimpleStringProperty();

        public UserRow(String id, String name, String username, String email,
                       String role, String status, String phone, String created) {
            this.id.set(id);
            this.name.set(name);
            this.username.set(username);
            this.email.set(email);
            this.role.set(role);
            this.status.set(status);
            this.phone.set(phone);
            this.created.set(created);
        }

        public String getId() { return id.get(); }
        public String getName() { return name.get(); }
        public String getUsername() { return username.get(); }
        public String getEmail() { return email.get(); }
        public String getRole() { return role.get(); }
        public String getStatus() { return status.get(); }
        public String getPhone() { return phone.get(); }
        public String getCreated() { return created.get(); }

        public void setStatus(String s) { status.set(s); }

        public javafx.beans.property.StringProperty idProperty() { return id; }
        public javafx.beans.property.StringProperty nameProperty() { return name; }
        public javafx.beans.property.StringProperty usernameProperty() { return username; }
        public javafx.beans.property.StringProperty emailProperty() { return email; }
        public javafx.beans.property.StringProperty roleProperty() { return role; }
        public javafx.beans.property.StringProperty statusProperty() { return status; }
        public javafx.beans.property.StringProperty phoneProperty() { return phone; }
        public javafx.beans.property.StringProperty createdProperty() { return created; }
    }

    public static class LogRow {
        private final javafx.beans.property.SimpleStringProperty time = new javafx.beans.property.SimpleStringProperty();
        private final javafx.beans.property.SimpleStringProperty actor = new javafx.beans.property.SimpleStringProperty();
        private final javafx.beans.property.SimpleStringProperty action = new javafx.beans.property.SimpleStringProperty();
        private final javafx.beans.property.SimpleStringProperty details = new javafx.beans.property.SimpleStringProperty();

        public LogRow(String time, String actor, String action, String details) {
            this.time.set(time);
            this.actor.set(actor);
            this.action.set(action);
            this.details.set(details);
        }

        public javafx.beans.property.StringProperty timeProperty() { return time; }
        public javafx.beans.property.StringProperty actorProperty() { return actor; }
        public javafx.beans.property.StringProperty actionProperty() { return action; }
        public javafx.beans.property.StringProperty detailsProperty() { return details; }
    }
}
