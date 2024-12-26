package com.kernelcrash.byte_bank.controller.dashboard;

import com.kernelcrash.byte_bank.MainApplication;
import com.kernelcrash.byte_bank.utils.SceneController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class DashboardLayoutController implements SceneController {
    Stage stage = MainApplication.sceneManager.primaryStage;

    @FXML
    private Button dashboardBtn;

    @FXML
    private Button transactionsBtn;

    @FXML
    private Button settingsBtn;

    @FXML
    private StackPane contentArea;

    @FXML
    public void loadDashboardView() {
        stage.setTitle("Dashboard | Byte Bank");
        clearActiveButtonStyles();
        dashboardBtn.getStyleClass().add("active-sidebar-button");
        loadView("fxml/dashboard/homeView.fxml");
    }

    @FXML
    public void loadTransactionsView() {
        stage.setTitle("Transactions | Byte Bank");
        clearActiveButtonStyles();
        transactionsBtn.getStyleClass().add("active-sidebar-button");
        loadView("fxml/dashboard/transactionsView.fxml");
    }

    @FXML
    public void loadSettingsView() {
        stage.setTitle("Settings | Byte Bank");
        clearActiveButtonStyles();
        settingsBtn.getStyleClass().add("active-sidebar-button");
        loadView("fxml/dashboard/SettingsView.fxml");
    }

    private void clearActiveButtonStyles() {
        dashboardBtn.getStyleClass().remove("active-sidebar-button");
        transactionsBtn.getStyleClass().remove("active-sidebar-button");
        settingsBtn.getStyleClass().remove("active-sidebar-button");
    }

    @FXML
    public void logout() {
        stage.setTitle("Login | Byte Bank");
        stage.setWidth(400);
        stage.setHeight(600);
        stage.setResizable(false);
        MainApplication.sceneManager.switchScene("login");
    }

    @FXML
    void initialize() {
        setupUI();
    }

    private void setupUI() {
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setResizable(true);
        System.out.println("DashboardLayoutController initialized");
        //loadDashboardView();
    }

    private void loadView(String fxmlFile) {
        try {
            Node content = FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource(fxmlFile)));

            if (contentArea != null && contentArea.getChildren().isEmpty()) {
                contentArea.getChildren().clear();
                contentArea.getChildren().setAll(content);
            } else {
                assert contentArea != null;
                contentArea.getChildren().setAll(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reinitialize() {
        setupUI();
    }
}
