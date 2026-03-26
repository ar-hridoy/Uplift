package com.uplift.desktop.controller.passenger;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class PassengerPaymentsController {

    @FXML private Label lblTotalThisMonth;
    @FXML private Label lblLastPayment;

    @FXML private TableView<PaymentRow> tblPayments;
    @FXML private TableColumn<PaymentRow, String> colDate;
    @FXML private TableColumn<PaymentRow, String> colRideId;
    @FXML private TableColumn<PaymentRow, String> colMethod;
    @FXML private TableColumn<PaymentRow, String> colAmount;
    @FXML private TableColumn<PaymentRow, String> colStatus;

    @FXML
    private void initialize() {
        colDate.setCellValueFactory(d -> d.getValue().dateProperty());
        colRideId.setCellValueFactory(d -> d.getValue().rideIdProperty());
        colMethod.setCellValueFactory(d -> d.getValue().methodProperty());
        colAmount.setCellValueFactory(d -> d.getValue().amountProperty());
        colStatus.setCellValueFactory(d -> d.getValue().statusProperty());

        ObservableList<PaymentRow> data = getSamplePayments();
        tblPayments.setItems(data);

        lblTotalThisMonth.setText("৳" + data.stream()
                .mapToInt(r -> Integer.parseInt(r.getAmount().replace("৳", "")))
                .sum());

        if (!data.isEmpty()) {
            lblLastPayment.setText(data.get(0).getAmount() + " (" + data.get(0).getDate() + ")");
        }
    }

    public static class PaymentRow {
        private final SimpleStringProperty date   = new SimpleStringProperty();
        private final SimpleStringProperty rideId = new SimpleStringProperty();
        private final SimpleStringProperty method = new SimpleStringProperty();
        private final SimpleStringProperty amount = new SimpleStringProperty();
        private final SimpleStringProperty status = new SimpleStringProperty();

        public PaymentRow(String date, String rideId, String method,
                          String amount, String status) {
            this.date.set(date);
            this.rideId.set(rideId);
            this.method.set(method);
            this.amount.set(amount);
            this.status.set(status);
        }

        public String getDate()   { return date.get(); }
        public String getRideId() { return rideId.get(); }
        public String getMethod() { return method.get(); }
        public String getAmount() { return amount.get(); }
        public String getStatus() { return status.get(); }

        public SimpleStringProperty dateProperty()   { return date; }
        public SimpleStringProperty rideIdProperty() { return rideId; }
        public SimpleStringProperty methodProperty() { return method; }
        public SimpleStringProperty amountProperty() { return amount; }
        public SimpleStringProperty statusProperty() { return status; }
    }

    private ObservableList<PaymentRow> getSamplePayments() {
        return FXCollections.observableArrayList(
                new PaymentRow("2025-12-11 09:40", "P-201", "Bkash", "180", "Completed"),
                new PaymentRow("2025-12-10 18:20", "P-198", "Card", "260", "Completed"),
                new PaymentRow("2025-12-09 14:05", "P-187", "Cash", "220", "Completed")
        );
    }
}
