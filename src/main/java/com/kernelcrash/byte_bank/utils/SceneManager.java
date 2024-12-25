package com.kernelcrash.byte_bank.utils;

import com.kernelcrash.byte_bank.MainApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class SceneManager {
    private final Stage primaryStage;
    private HashMap<String, FXMLLoader> loaders = new HashMap<>();

    public SceneManager(Stage stage) {
        this.primaryStage = stage;
    }

    public void loadScene(String name, String fxmlPath) throws IOException {
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
                }
                primaryStage.setTitle("Byte Bank");
                primaryStage.setResizable(name.equals("dashboard"));
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
}

