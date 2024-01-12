package com.exemple.codehive;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        primaryStage.setTitle("Crypto Wallet Login");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    /*@Override
    public void start(Stage primaryStage) {
        try {
            // Charge le fichier FXML pour l'interface utilisateur des cryptomonnaies
            Parent cryptoRoot = FXMLLoader.load(getClass().getResource("/fxml/Crypto.fxml"));
            Scene cryptoScene = new Scene(cryptoRoot);
            Stage cryptoStage = new Stage();
            cryptoStage.setTitle("Crypto Viewer");
            cryptoStage.setScene(cryptoScene);
            cryptoStage.show();

            // Charger le fichier FXML pour l'interface utilisateur des actions

            /*Parent actionRoot = FXMLLoader.load(getClass().getResource("/fxml/Action.fxml"));
            Scene actionScene = new Scene(actionRoot);
            Stage actionStage = new Stage();
            actionStage.setTitle("Action");
            actionStage.setScene(actionScene);
            actionStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/



    public static void main(String[] args) {
        launch(args);
    }
}
