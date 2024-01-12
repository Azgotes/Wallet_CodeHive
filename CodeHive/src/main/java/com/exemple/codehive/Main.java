package com.exemple.codehive;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.CoinGeckoService;
import model.AlphaVantageService;
import model.ExcelWriter;
import java.util.Map;

public class Main /*extends Application*/ {

    /*@Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        primaryStage.setTitle("Crypto Wallet Login");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }*/

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

            Parent actionRoot = FXMLLoader.load(getClass().getResource("/fxml/Action.fxml"));
            Scene actionScene = new Scene(actionRoot);
            Stage actionStage = new Stage();
            actionStage.setTitle("Action");
            actionStage.setScene(actionScene);
            actionStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }*/
    public static void main(String[] args) {
        // Création des instances des services
        CoinGeckoService coinGeckoService = new CoinGeckoService();
        AlphaVantageService alphaVantageService = new AlphaVantageService();

        // Récupération des données
        Map<String, Double> cryptoPrices = coinGeckoService.getAllCryptoPrices();
        Map<String, Double> stockPrices = alphaVantageService.getAllStockPrices();

        // Affichage des données récupérées pour vérification
        System.out.println("Crypto Prices: " + cryptoPrices);
        System.out.println("Stock Prices: " + stockPrices);

        // Création de l'instance ExcelWriter et écriture des données dans le fichier Excel
        ExcelWriter excelWriter = new ExcelWriter();
        String excelFilePath = "./Files/ActionCrypto.xlsx";

        try {
            excelWriter.writeDataToExcel(cryptoPrices, stockPrices, excelFilePath);
            System.out.println("Data written successfully to Excel file.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to write data to Excel file.");
        }
    }

}
