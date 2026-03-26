package com.uplift.desktop.controller.passenger;

import com.uplift.desktop.util.AlertUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.time.LocalTime;

public class PassengerRequestRideController {

    @FXML private RadioButton rbInstant;
    @FXML private RadioButton rbFuture;
    private ToggleGroup rideTypeGroup;

    @FXML private TextField txtFrom;
    @FXML private TextField txtTo;
    @FXML private Spinner<Integer> spSeats;
    @FXML private TextArea txtNote;

    @FXML private VBox futureBox;
    @FXML private DatePicker dpDate;
    @FXML private TextField txtTime;

    @FXML private Label lblPreview;
    @FXML private Label lblFare;

    @FXML
    private void initialize() {
        // ride type toggle
        rideTypeGroup = new ToggleGroup();
        rbInstant.setToggleGroup(rideTypeGroup);
        rbFuture.setToggleGroup(rideTypeGroup);
        rbInstant.setSelected(true);
        spSeats.setEditable(true);

        // seats spinner
        spSeats.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 6, 1));

        // show/hide future box
        rideTypeGroup.selectedToggleProperty().addListener((obs, oldV, newV) -> {
            boolean future = rbFuture.isSelected();
            futureBox.setVisible(future);
            futureBox.setManaged(future);
            updatePreview();
        });

        txtFrom.textProperty().addListener((a,b,c) -> updatePreview());
        txtTo.textProperty().addListener((a,b,c) -> updatePreview());
        spSeats.valueProperty().addListener((a,b,c) -> updatePreview());
        txtTime.textProperty().addListener((a,b,c) -> updatePreview());
        if (dpDate != null) dpDate.valueProperty().addListener((a,b,c) -> updatePreview());

        updatePreview();
    }

    private void updatePreview() {
        String type = rbFuture.isSelected() ? "FUTURE" : "INSTANT";
        String from = txtFrom.getText().trim();
        String to = txtTo.getText().trim();
        int seats = spSeats.getValue();

        StringBuilder sb = new StringBuilder();
        sb.append("Type: ").append(type).append("\n");
        sb.append("From: ").append(from.isEmpty() ? "-" : from).append("\n");
        sb.append("To: ").append(to.isEmpty() ? "-" : to).append("\n");
        sb.append("Seats: ").append(seats).append("\n");

        if (rbFuture.isSelected()) {
            sb.append("Date: ").append(dpDate.getValue() == null ? "-" : dpDate.getValue()).append("\n");
            sb.append("Time: ").append(txtTime.getText().trim().isEmpty() ? "-" : txtTime.getText().trim()).append("\n");
        }

        lblPreview.setText(sb.toString());

        // dummy fare estimate (replace later with backend)
        int base = 80;
        int perSeat = 20 * seats;
        lblFare.setText("৳" + (base + perSeat));
    }

    @FXML
    private void handleSubmit() {
        String from = txtFrom.getText().trim();
        String to = txtTo.getText().trim();

        if (from.isEmpty() || to.isEmpty()) {
            AlertUtils.showError("Validation", "From and To are required.");
            return;
        }

        if (rbFuture.isSelected()) {
            if (dpDate.getValue() == null) {
                AlertUtils.showError("Validation", "Please select a date for future ride.");
                return;
            }
            String t = txtTime.getText().trim();
            try {
                LocalTime.parse(t); // expects HH:mm
            } catch (Exception e) {
                AlertUtils.showError("Validation", "Time must be HH:mm (e.g. 18:30).");
                return;
            }
        }

        AlertUtils.showInfo("Requested", "Ride request submitted (dummy).");
        handleReset();
    }

    @FXML
    private void handleReset() {
        rbInstant.setSelected(true);
        txtFrom.clear();
        txtTo.clear();
        txtNote.clear();
        spSeats.getValueFactory().setValue(1);
        if (dpDate != null) dpDate.setValue(null);
        txtTime.clear();
        updatePreview();
    }
}
