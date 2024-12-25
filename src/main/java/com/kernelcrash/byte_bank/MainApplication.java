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
        sceneManager.loadScene("dashboard", "dashboard.fxml");
        sceneManager.loadScene("login", "onboarding/login.fxml");
        sceneManager.loadScene("signup", "onboarding/signup.fxml");

        // Set the initial scene
        sceneManager.switchScene("login");

        stage.setTitle("Advanced Multi-Screen App");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
