package com.kernelcrash.byte_bank;

import com.kernelcrash.byte_bank.utils.HttpClientHelper;
import com.kernelcrash.byte_bank.utils.SceneManager;
import com.kernelcrash.byte_bank.utils.StateManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApplication extends Application {
    public static SceneManager sceneManager;
    public static HttpClientHelper httpClientHelper;

    @Override
    public void start(Stage stage) throws Exception {
        httpClientHelper = new HttpClientHelper();
        StateManager stateManager = StateManager.getInstance();
        sceneManager = new SceneManager(stage);
        sceneManager.init();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
