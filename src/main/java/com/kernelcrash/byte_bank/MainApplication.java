package com.kernelcrash.byte_bank;

import com.kernelcrash.byte_bank.utils.*;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.stage.Stage;

import java.util.concurrent.ExecutionException;

public class MainApplication extends Application {
    public static HttpClientHelper httpClientHelper;
    public static StateManager stateManager;
    public static SceneManager sceneManager;

    @Override
    public void start(Stage stage) throws Exception {
        ConfigHelper.init();
        httpClientHelper = new HttpClientHelper();
        stateManager = StateManager.getInstance();

        if(stateManager.isUserLoggedIn()) {
            loadMarketWS();
        }

        sceneManager = new SceneManager(stage);
        sceneManager.init();
    }

    private static void loadMarketWS() {
        Task<Void> loadTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    StompClient.fetchCryptoDataFeed();
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
            }

            @Override
            protected void failed() {
                super.failed();
            }
        };

        Thread loadThread = new Thread(loadTask);
        loadThread.setDaemon(true);
        loadThread.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
