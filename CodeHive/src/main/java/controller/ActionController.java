package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import model.Action;
import model.ExcelReader;
import model.StartupManager;

import java.io.IOException;
import java.util.Map;

public class ActionController {

    @FXML
    private TableView<Action> actionTable;

    @FXML
    private TableColumn<Action, String> symbolColumn;

    @FXML
    private TableColumn<Action, Double> priceColumn;
    private final StartupManager startupManager = new StartupManager();


    private final ExcelReader excelReader = new ExcelReader();

    @FXML
    public void initialize() {
        symbolColumn.setCellValueFactory(new PropertyValueFactory<>("symbol"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        loadActionData();
    }

    private void loadActionData() {
        ObservableList<Action> actions = FXCollections.observableArrayList();

        try {
            Map<String, Double> prices = excelReader.readPrices("./Files/ActionCrypto.xlsx", "Stocks");
            prices.forEach((symbol, price) -> {
                actions.add(new Action(symbol, price));
            });
            actionTable.setItems(actions);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, possibly with a user alert
        }
    }

    @FXML
    protected void handleBackButtonAction(ActionEvent event) {
        navigateToWallet(event);
    }

    private void navigateToWallet(ActionEvent event) {
        try {
            Parent walletView = FXMLLoader.load(getClass().getResource("/fxml/Wallet.fxml"));
            Scene walletScene = new Scene(walletView);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(walletScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gestion des erreurs
        }
    }

    @FXML
    protected void handleRefreshButtonAction(ActionEvent event) {
        startupManager.initializeApplicationData();
        loadActionData();
    }
}
