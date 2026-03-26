package com.uplift.desktop.controller.admin;

import com.uplift.desktop.util.AlertUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AdminPaymentsController {

    @FXML private Label lblTodayRevenue, lblWeekRevenue, lblMonthRevenue, lblDisputes;

    @FXML private TextField txtSearch;
    @FXML private ComboBox<String> cmbStatus;
    @FXML private ComboBox<String> cmbRange;

    @FXML private TableView<TxRow> tblTransactions;
    @FXML private TableColumn<TxRow, String> colTrxId, colRideId, colPayer, colAmount, colMethod, colStatus, colDate;

    private final ObservableList<TxRow> data = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // cards (dummy)
        lblTodayRevenue.setText("$1,240");
        lblWeekRevenue.setText("$6,820");
        lblMonthRevenue.setText("$22,410");
        lblDisputes.setText("2");

        cmbStatus.setItems(FXCollections.observableArrayList("ALL", "PAID", "PENDING", "FAILED", "DISPUTED", "REFUNDED"));
        cmbStatus.getSelectionModel().select("ALL");

        cmbRange.setItems(FXCollections.observableArrayList("TODAY", "LAST 7 DAYS", "LAST 30 DAYS", "THIS MONTH"));
        cmbRange.getSelectionModel().select("LAST 7 DAYS");

        colTrxId.setCellValueFactory(x -> x.getValue().trxId);
        colRideId.setCellValueFactory(x -> x.getValue().rideId);
        colPayer.setCellValueFactory(x -> x.getValue().payer);
        colAmount.setCellValueFactory(x -> x.getValue().amount);
        colMethod.setCellValueFactory(x -> x.getValue().method);
        colStatus.setCellValueFactory(x -> x.getValue().status);
        colDate.setCellValueFactory(x -> x.getValue().date);

        data.addAll(
                new TxRow("TRX-88312", "P-201", "rahim242-15-062@diu.edu.bd", "৳180", "BKASH", "PAID", "2025-12-11 09:35"),
                new TxRow("TRX-88329", "P-202", "passenger2@uplift.com", "৳220", "NAGAD", "DISPUTED", "2025-12-12 12:05"),
                new TxRow("TRX-88351", "I-112", "passenger3@uplift.com", "৳140", "CASH", "PAID", "2025-12-12 13:10")
        );

        tblTransactions.setItems(data);
    }

    @FXML
    private void handleApplyFilters(ActionEvent e) {
        // TODO: call backend with filters. For now just info.
        AlertUtils.showInfo("Filters", "Filters applied (dummy).");
    }

    @FXML
    private void handleExportCsv(ActionEvent e) {
        AlertUtils.showInfo("Export", "CSV export not implemented yet.");
    }

    @FXML
    private void handleRefund(ActionEvent e) {
        TxRow r = tblTransactions.getSelectionModel().getSelectedItem();
        if (r == null) { AlertUtils.showError("No selection", "Select a transaction first."); return; }
        AlertUtils.showInfo("Refund", "Pretend refund for " + r.trxId.get());
    }

    @FXML
    private void handleResolveDispute(ActionEvent e) {
        TxRow r = tblTransactions.getSelectionModel().getSelectedItem();
        if (r == null) { AlertUtils.showError("No selection", "Select a transaction first."); return; }
        AlertUtils.showInfo("Dispute", "Marked resolved for " + r.trxId.get());
    }

    public static class TxRow {
        SimpleStringProperty trxId = new SimpleStringProperty();
        SimpleStringProperty rideId = new SimpleStringProperty();
        SimpleStringProperty payer = new SimpleStringProperty();
        SimpleStringProperty amount = new SimpleStringProperty();
        SimpleStringProperty method = new SimpleStringProperty();
        SimpleStringProperty status = new SimpleStringProperty();
        SimpleStringProperty date = new SimpleStringProperty();

        public TxRow(String trxId, String rideId, String payer, String amount, String method, String status, String date) {
            this.trxId.set(trxId);
            this.rideId.set(rideId);
            this.payer.set(payer);
            this.amount.set(amount);
            this.method.set(method);
            this.status.set(status);
            this.date.set(date);
        }
    }
}
