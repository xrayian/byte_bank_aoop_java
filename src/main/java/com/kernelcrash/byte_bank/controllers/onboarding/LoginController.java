package com.kernelcrash.byte_bank.controllers.onboarding;

import com.kernelcrash.byte_bank.MainApplication;
import com.kernelcrash.byte_bank.utils.SceneController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import com.kernelcrash.byte_bank.utils.SceneManager;
import javafx.stage.Stage;

public class LoginController implements SceneController {
    Stage stage = MainApplication.sceneManager.primaryStage;

    @FXML
    private Label signup_btn;
    @FXML
    private Button login_btn;

    @FXML
    private void login() {
        //todo implement login
        SceneManager sceneManager = MainApplication.sceneManager;
        sceneManager.switchScene("dashboard/root");
    }

    @FXML
    private void initialize() {
        setupUI();
        signup_btn.setOnMouseClicked(event -> {
            SceneManager sceneManager = MainApplication.sceneManager;
            sceneManager.switchScene("signup");
        });
    }

    private void setupUI() {
        stage.setTitle("Login | Byte Bank");
        stage.setWidth(400);
        stage.setHeight(600);
        stage.setResizable(false);
    }


    @Override
    public void reinitialize() {
        setupUI();
    }
}
