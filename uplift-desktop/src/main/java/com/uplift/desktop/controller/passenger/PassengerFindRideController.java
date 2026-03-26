package com.uplift.desktop.controller.passenger;

import com.uplift.desktop.util.AlertUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class PassengerFindRideController {

    @FXML private TextField txtFromFilter;
    @FXML private TextField txtToFilter;
    @FXML private DatePicker dpFilterDate;

    @FXML private TableView<RideRow> tblRides;
    @FXML private TableColumn<RideRow, String> colDriver;
    @FXML private TableColumn<RideRow, String> colFrom;
    @FXML private TableColumn<RideRow, String> colTo;
    @FXML private TableColumn<RideRow, String> colTime;
    @FXML private TableColumn<RideRow, String> colPrice;
    @FXML private TableColumn<RideRow, String> colSeats;

    @FXML private Label lblInfo;

    @FXML
    private void initialize() {
        colDriver.setCellValueFactory(d -> d.getValue().driverProperty());
        colFrom.setCellValueFactory(d -> d.getValue().fromProperty());
        colTo.setCellValueFactory(d -> d.getValue().toProperty());
        colTime.setCellValueFactory(d -> d.getValue().timeProperty());
        colPrice.setCellValueFactory(d -> d.getValue().priceProperty());
        colSeats.setCellValueFactory(d -> d.getValue().seatsProperty());

        tblRides.setItems(getSampleRides());
    }

    @FXML
    private void handleSearch() {
        String from = txtFromFilter.getText().trim();
        String to   = txtToFilter.getText().trim();
        LocalDate date = dpFilterDate.getValue();

        // TODO: call backend: /api/passenger/rides/search?from=&to=&date=
        lblInfo.setText("Showing demo results for: " + from + " → " + to +
                (date != null ? " on " + date : ""));
        tblRides.setItems(getSampleRides());
    }

    @FXML
    private void handleJoinRide() {
        RideRow row = tblRides.getSelectionModel().getSelectedItem();
        if (row == null) {
            AlertUtils.showError("No ride selected",
                    "Please select a ride first.");
            return;
        }

        // TODO: call backend to request seat on that ride
        AlertUtils.showInfo("Seat requested",
                "Seat requested on ride with " + row.getDriver() + " (" + row.getFrom() + " → " + row.getTo() + ")");
    }

    /* model + sample data */

    public static class RideRow {
        private final SimpleStringProperty driver = new SimpleStringProperty();
        private final SimpleStringProperty from   = new SimpleStringProperty();
        private final SimpleStringProperty to     = new SimpleStringProperty();
        private final SimpleStringProperty time   = new SimpleStringProperty();
        private final SimpleStringProperty price  = new SimpleStringProperty();
        private final SimpleStringProperty seats  = new SimpleStringProperty();

        public RideRow(String driver, String from, String to,
                       String time, String price, String seats) {
            this.driver.set(driver);
            this.from.set(from);
            this.to.set(to);
            this.time.set(time);
            this.price.set(price);
            this.seats.set(seats);
        }

        public String getDriver() { return driver.get(); }
        public String getFrom()   { return from.get(); }
        public String getTo()     { return to.get(); }
        public String getTime()   { return time.get(); }
        public String getPrice()  { return price.get(); }
        public String getSeats()  { return seats.get(); }

        public SimpleStringProperty driverProperty() { return driver; }
        public SimpleStringProperty fromProperty()   { return from; }
        public SimpleStringProperty toProperty()     { return to; }
        public SimpleStringProperty timeProperty()   { return time; }
        public SimpleStringProperty priceProperty()  { return price; }
        public SimpleStringProperty seatsProperty()  { return seats; }
    }

    private ObservableList<RideRow> getSampleRides() {
        return FXCollections.observableArrayList(
                new RideRow("Rahim", "Uttara", "Dhanmondi", "10:30", "৳180", "2"),
                new RideRow("Karim", "Mirpur", "Banani", "11:00", "৳220", "1"),
                new RideRow("Sadia", "Bashundhara", "Motijheel", "12:15", "৳250", "3")
        );
    }
}

