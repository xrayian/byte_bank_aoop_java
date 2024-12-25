package com.kernelcrash.byte_bank.controller.onboarding;

import com.kernelcrash.byte_bank.MainApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import com.kernelcrash.byte_bank.utils.SceneManager;

public class LoginController {
    @FXML
    private TextField inputField;
    @FXML
    private Button goToScreen2Button;

    @FXML
    private void initialize() {
        goToScreen2Button.setOnAction(event -> {
            String data = inputField.getText();

            // Pass data to Screen2Controller
            SignupController signupController = (SignupController) MainApplication.sceneManager.getController("signup");
            signupController.setData(data);

            MainApplication.sceneManager.switchScene("signup");
        });
    }
}
