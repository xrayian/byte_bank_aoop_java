package com.kernelcrash.byte_bank.controller.onboarding;

import com.kernelcrash.byte_bank.MainApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class SignupController {
    @FXML
    private Label dataLabel;
    @FXML
    private Button goBackButton;

    @FXML
    private void initialize() {
        goBackButton.setOnAction(event -> MainApplication.sceneManager.switchScene("login"));
    }

    public void setData(String data) {
        dataLabel.setText(data);
    }
}

