package com.uplift.desktop.controller.admin;

import com.uplift.desktop.util.AlertUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;


import java.util.List;

public class AdminVehicleDocsController {

    @FXML private Label lblTitle;
    @FXML private Label lblSubtitle;
    @FXML private Label lblPreview;
    @FXML private ListView<String> listDocs;

    // call this from AdminVehiclesController
    public void init(String vehicleId, String riderName, List<String> docs) {
        lblSubtitle.setText("Vehicle: " + vehicleId + " • Rider: " + riderName);

        if (docs == null || docs.isEmpty()) {
            listDocs.setItems(FXCollections.observableArrayList("No documents uploaded"));
        } else {
            listDocs.setItems(FXCollections.observableArrayList(docs));
        }

        listDocs.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                lblPreview.setText("Selected: " + newVal + "\n\n(Preview hookup optional later)");
            }
        });
    }

    @FXML
    private void handleOpen() {
        String selected = listDocs.getSelectionModel().getSelectedItem();
        if (selected == null || selected.equals("No documents uploaded")) {
            AlertUtils.showError("No document", "Select a document first.");
            return;
        }

        // Later: open URL / download / show Image/PDF viewer.
        AlertUtils.showInfo("Open Document", "TODO: open document: " + selected);
    }

    @FXML
    private void handleClose() {
        Stage s = (Stage) lblTitle.getScene().getWindow();
        s.close();
    }
}
