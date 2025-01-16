package com.kernelcrash.byte_bank.controllers.onboarding;

import com.kernelcrash.byte_bank.MainApplication;
import com.kernelcrash.byte_bank.utils.ConfigHelper;
import com.kernelcrash.byte_bank.utils.SceneController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class SignupController implements SceneController {

    Stage stage = MainApplication.sceneManager.primaryStage;

    @FXML
    private TextField fname;

    @FXML
    private TextField lname;

    @FXML
    private TextField email;

    @FXML
    private TextField pwd;

    @FXML
    private TextField pwd_re;

    @FXML
    private Label error_msg;


    @FXML
    private Label login_btn;
    @FXML
    private Button signup_btn;

    @FXML
    private void signup() throws Exception {
        error_msg.setText("");
        String username = fname.getText() + " " + lname.getText();
        String password = pwd.getText();
        String email = this.email.getText();
        if (!password.equals(pwd_re.getText())) {
            error_msg.setText("Passwords do not match");
            return;
        }

        String jsonBody = "{ \"username\": \"" + username + "\", \"email\": \"" + email + "\", \"password\": \"" + password + "\" }";
        String empty = "";

        username = username.trim().replace(" ", "%20");
        password = URLEncoder.encode(pwd.getText(), StandardCharsets.UTF_8);
        email = email.trim().replace(" ", "%20");

        //send http post request
        try {
            MainApplication.httpClientHelper.sendPost(ConfigHelper.BACKEND_API_URL +"auth/register?username="+username+"&password="+password+"&email="+email, empty, Map.of("Content-Type", "application/json"));
        } catch (Exception e) {
            e.printStackTrace();
            error_msg.setText("An error occurred. Please try again later.");
            return;
        }
        new Alert(Alert.AlertType.INFORMATION, "Account created successfully").showAndWait();
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
//        stage.setWidth(400);
//        stage.setHeight(600);
        stage.setResizable(false);
    }

    @Override
    public void reinitialize() {
        setupUI();
    }


}

