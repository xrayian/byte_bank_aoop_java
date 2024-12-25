package com.kernelcrash.byte_bank;

import com.kernelcrash.byte_bank.utils.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApplication extends Application {
    public static SceneManager sceneManager;

    @Override
    public void start(Stage stage) throws Exception {
        sceneManager = new SceneManager(stage);
        // Load scenes

        sceneManager.init();

        sceneManager.loadScene("dashboard", "fxml/dashboard.fxml");
        sceneManager.loadScene("login", "fxml/onboarding/login.fxml");
        sceneManager.loadScene("signup", "fxml/onboarding/signup.fxml");

        // Set the initial scene
        sceneManager.switchScene("login");

    }

    public static void main(String[] args) {
        launch(args);
    }
}
