package com.exemple.codehive;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Since Login.fxml is in the package com.exemple.codehive.fxml,
        // the path is relative to the root of the classpath (src/main/resources)
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        primaryStage.setTitle("Crypto Wallet Login");
        primaryStage.setScene(new Scene(root, 600, 400)); // You can adjust the size as needed
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
