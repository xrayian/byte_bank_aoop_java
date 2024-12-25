package com.kernelcrash.byte_bank.controller.dashboard;

import com.kernelcrash.byte_bank.MainApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Objects;

public class DashboardLayoutController {

    @FXML
    private StackPane contentArea;

    @FXML
    public void loadDashboardView() {
        loadView("fxml/dashboard/homeView.fxml");
    }

    @FXML
    public void loadTransactionsView() {
        loadView("fxml/dashboard/transactionsView.fxml");
    }

    @FXML
    public void loadSettingsView() {
        loadView("fxml/dashboard/SettingsView.fxml");
    }

    @FXML
    public void logout() {
        MainApplication.sceneManager.switchScene("login");
    }

    @FXML
    void initialize() {
        loadDashboardView();
    }

    private void loadView(String fxmlFile) {
        try {
            Node view = FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource(fxmlFile)));

            if (contentArea != null && contentArea.getChildren().isEmpty()) {
                contentArea.getChildren().clear();
                contentArea.getChildren().setAll(view);
            } else {
                assert contentArea != null;
                contentArea.getChildren().setAll(view);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
