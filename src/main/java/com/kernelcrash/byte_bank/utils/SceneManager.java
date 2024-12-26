package com.kernelcrash.byte_bank.utils;

import com.kernelcrash.byte_bank.MainApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class SceneManager {
    public final Stage primaryStage;
    private HashMap<String, FXMLLoader> loaders = new HashMap<>();

    public SceneManager(Stage stage) {
        this.primaryStage = stage;
    }

    private void loadScene(String name, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource(fxmlPath));
        loaders.put(name, loader);
    }

    public void switchScene(String name) {
        FXMLLoader loader = loaders.get(name);
        if (loader != null) {
            try {
                if (primaryStage.getScene() == null) {
                    primaryStage.setScene(new Scene(loader.load()));
                } else if (loader.getRoot() == null) {
                    primaryStage.getScene().setRoot(loader.load());
                } else {
                    primaryStage.getScene().setRoot(loader.getRoot());
                    SceneController sc = loader.getController();
                    sc.reinitialize();

                }
                primaryStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Scene not found: " + name);
        }
    }

    public Object getController(String name) {
        FXMLLoader loader = loaders.get(name);
        return (loader != null) ? loader.getController() : null;
    }

    private void loadApplicationScenes() throws IOException {
        loadScene("dashboard/root", "fxml/dashboard/layout.fxml");
        loadScene("login", "fxml/onboarding/login.fxml");
        loadScene("signup", "fxml/onboarding/signup.fxml");
        loadScene("dashboard/home", "fxml/dashboard/homeView.fxml");
        loadScene("dashboard/transactions", "fxml/dashboard/transactionsView.fxml");
        loadScene("dashboard/settings", "fxml/dashboard/settingsView.fxml");
    }

    private void navigateToInitialScene() {
        try {
            //todo: check if user is logged in
            switchScene("login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() throws IOException {
        //set icon
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(MainApplication.class.getResourceAsStream("/com/kernelcrash/byte_bank/images/icon.png"))));
        primaryStage.setTitle("Byte Bank Client");
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(600);

        loadApplicationScenes();
        navigateToInitialScene();
    }
}

