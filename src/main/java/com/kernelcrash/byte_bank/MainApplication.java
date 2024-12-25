package com.kernelcrash.byte_bank;

import com.kernelcrash.byte_bank.utils.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApplication extends Application {
    public static SceneManager sceneManager;

    @Override
    public void start(Stage stage) throws Exception {
        sceneManager = new SceneManager(stage);
        sceneManager.init();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
