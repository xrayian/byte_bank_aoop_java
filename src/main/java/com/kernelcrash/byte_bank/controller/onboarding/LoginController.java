package com.kernelcrash.byte_bank.controller.onboarding;

import com.kernelcrash.byte_bank.MainApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.kernelcrash.byte_bank.utils.SceneManager;

public class LoginController {

    @FXML
    private Label signup_btn;
    @FXML
    private Button login_btn;

    @FXML
    private void login() {
        //todo implement login
        SceneManager sceneManager = MainApplication.sceneManager;
        sceneManager.switchScene("dashboard");
    }

    @FXML
    private void initialize() {
        signup_btn.setOnMouseClicked(event -> {
            SceneManager sceneManager = MainApplication.sceneManager;
            sceneManager.switchScene("signup");
        });
    }
}
