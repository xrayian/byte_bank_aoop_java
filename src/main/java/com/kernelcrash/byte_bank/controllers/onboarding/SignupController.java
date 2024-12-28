package com.kernelcrash.byte_bank.controllers.onboarding;

import com.kernelcrash.byte_bank.MainApplication;
import com.kernelcrash.byte_bank.utils.SceneController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class SignupController implements SceneController {

    Stage stage = MainApplication.sceneManager.primaryStage;

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
        setupUI();
        login_btn.setOnMouseClicked(event -> {
            MainApplication.sceneManager.switchScene("login");
        });
    }

    private void setupUI() {
        stage.setTitle("Create Account | Byte Bank");
        stage.setWidth(400);
        stage.setHeight(600);
        stage.setResizable(false);
    }

    @Override
    public void reinitialize() {
        setupUI();
    }


}

