package com.kernelcrash.byte_bank.controllers.onboarding;

import com.google.gson.*;
import com.kernelcrash.byte_bank.MainApplication;
import com.kernelcrash.byte_bank.models.User;
import com.kernelcrash.byte_bank.utils.ConfigHelper;
import com.kernelcrash.byte_bank.utils.SceneController;
import com.kernelcrash.byte_bank.utils.StateManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import com.kernelcrash.byte_bank.utils.SceneManager;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;

public class LoginController implements SceneController {
    Stage stage = MainApplication.sceneManager.primaryStage;

    @FXML
    private Label signup_btn;
    @FXML
    private Button login_btn;
    @FXML
    private Label error_msg;
    @FXML
    private TextField email;
    @FXML
    private TextField pwd;


    @FXML
    private void login() {
        error_msg.setText("");

        String email = this.email.getText();
        String password = pwd.getText();

        if (email.isEmpty() || password.isEmpty()) {
            error_msg.setText("Please fill in all fields");
            return;
        }

        password = URLEncoder.encode(pwd.getText(), StandardCharsets.UTF_8);
        email = email.trim().replace(" ", "%20");

        String response = "";
        //send http post request
        try {
            response = MainApplication.httpClientHelper.sendPost(ConfigHelper.getBACKEND_API_URL() + "auth/login?email=" + email + "&password=" + password, "", Map.of("Content-Type", "application/json"));
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
            error_msg.setText("An error occurred. Please try again later.");
            return;
        }


        Gson gson = ConfigHelper.GsonWithLocalDateTimeImpl();
        User user = gson.fromJson(response, User.class);
        if (user == null) {
            error_msg.setText("Invalid email or password");
            return;
        }
//        if (user.isActive() == false) {
//            error_msg.setText("Your account is not active. Please contact support.");
//            return;
//        }


        //store token
        StateManager stateManager = StateManager.getInstance();
        stateManager.setCurrentUser(user);

        //store user object
        ConfigHelper.storeLoggedInUserObject();


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
//        stage.setWidth(400);
//        stage.setHeight(600);
        stage.setResizable(false);
    }


    @Override
    public void reinitialize() {
        setupUI();
    }
}
