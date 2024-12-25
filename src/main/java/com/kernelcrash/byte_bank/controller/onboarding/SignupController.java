package com.kernelcrash.byte_bank.controller.onboarding;

import com.kernelcrash.byte_bank.MainApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class SignupController {

        @FXML
        private Label login_btn;
        @FXML
        private Button signup_btn;

        @FXML
        private void signup() {
            //todo implement signup
            MainApplication.sceneManager.switchScene("login");
        }

        @FXML
        private void initialize() {
            login_btn.setOnMouseClicked(event -> {
                MainApplication.sceneManager.switchScene("login");
            });
        }
}

