package com.uplift.desktop.controller.admin;

import com.uplift.desktop.util.AlertUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AdminSystemController {

    @FXML private TextField txtBaseFare, txtPerKm, txtServiceFee, txtSeatPrice;
    @FXML private CheckBox chkEmailOtp, chkRideNotifications, chkMaintenanceMode;
    @FXML private TextArea txtLogs;

    @FXML
    private void initialize() {
        // dummy defaults
        txtBaseFare.setText("50");
        txtPerKm.setText("15");
        txtServiceFee.setText("10");
        txtSeatPrice.setText("80");

        chkEmailOtp.setSelected(true);
        chkRideNotifications.setSelected(true);
        chkMaintenanceMode.setSelected(false);

        txtLogs.setText("[INFO] System started\n[INFO] Scheduler running\n[WARN] No backups found");
    }

    @FXML
    private void handleSavePricing(ActionEvent e) {
        AlertUtils.showInfo("Saved", "Pricing rules saved (dummy).");
    }

    @FXML
    private void handleResetPricing(ActionEvent e) {
        txtBaseFare.setText("50");
        txtPerKm.setText("15");
        txtServiceFee.setText("10");
        txtSeatPrice.setText("80");
        AlertUtils.showInfo("Reset", "Pricing reset to defaults.");
    }

    @FXML
    private void handleBackup(ActionEvent e) {
        AlertUtils.showInfo("Backup", "Backup started (dummy).");
    }

    @FXML
    private void handleRestore(ActionEvent e) {
        AlertUtils.showInfo("Restore", "Restore not implemented yet.");
    }

    @FXML
    private void handleRefreshLogs(ActionEvent e) {
        txtLogs.appendText("\n[INFO] Logs refreshed at runtime (dummy)");
    }
}
