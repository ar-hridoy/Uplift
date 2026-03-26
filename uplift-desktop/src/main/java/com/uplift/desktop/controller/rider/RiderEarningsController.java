package com.uplift.desktop.controller.rider;

import com.uplift.desktop.util.AlertUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class RiderEarningsController {

    @FXML private Label lblToday, lblWeek, lblMonth, lblPending;

    @FXML private ComboBox<String> cmbRange;
    @FXML private ComboBox<String> cmbStatus;

    @FXML private TableView<Row> tblPayouts;
    @FXML private TableColumn<Row, String> colId, colAmount, colMethod, colStatus, colDate, colNote;

    private final ObservableList<Row> data = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // dummy summary
        lblToday.setText("$320");
        lblWeek.setText("$1,480");
        lblMonth.setText("$4,220");
        lblPending.setText("$180");

        cmbRange.setItems(FXCollections.observableArrayList("TODAY", "LAST 7 DAYS", "LAST 30 DAYS", "THIS MONTH"));
        cmbRange.getSelectionModel().select("LAST 7 DAYS");

        cmbStatus.setItems(FXCollections.observableArrayList("ALL", "PAID", "PENDING", "FAILED"));
        cmbStatus.getSelectionModel().select("ALL");

        colId.setCellValueFactory(x -> x.getValue().id);
        colAmount.setCellValueFactory(x -> x.getValue().amount);
        colMethod.setCellValueFactory(x -> x.getValue().method);
        colStatus.setCellValueFactory(x -> x.getValue().status);
        colDate.setCellValueFactory(x -> x.getValue().date);
        colNote.setCellValueFactory(x -> x.getValue().note);

        data.addAll(
                new Row("PO-9001", "$250", "bKash", "PAID", "2025-12-10 18:20", "Weekly payout"),
                new Row("PO-9002", "$180", "Nagad", "PENDING", "2025-12-12 20:10", "Requested payout"),
                new Row("PO-9003", "$120", "Cash", "PAID", "2025-12-09 14:05", "Manual adjustment")
        );

        tblPayouts.setItems(data);
    }

    @FXML
    private void handleRequestPayout(ActionEvent e) {
        AlertUtils.showInfo("Payout", "Payout request sent (dummy).");
    }

    public static class Row {
        SimpleStringProperty id = new SimpleStringProperty();
        SimpleStringProperty amount = new SimpleStringProperty();
        SimpleStringProperty method = new SimpleStringProperty();
        SimpleStringProperty status = new SimpleStringProperty();
        SimpleStringProperty date = new SimpleStringProperty();
        SimpleStringProperty note = new SimpleStringProperty();

        public Row(String id, String amount, String method, String status, String date, String note) {
            this.id.set(id);
            this.amount.set(amount);
            this.method.set(method);
            this.status.set(status);
            this.date.set(date);
            this.note.set(note);
        }
    }
}
