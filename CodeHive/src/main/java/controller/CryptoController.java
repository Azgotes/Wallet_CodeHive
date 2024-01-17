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
import model.Crypto;
import model.ExcelReader;
import model.StartupManager;

import java.io.IOException;
import java.util.Map;

public class CryptoController {

    @FXML
    private TableView<Crypto> cryptoTable;

    @FXML
    private TableColumn<Crypto, String> nameColumn;

    @FXML
    private TableColumn<Crypto, Double> priceColumn;

    private final ExcelReader excelReader = new ExcelReader();

    private final StartupManager startupManager = new StartupManager();


    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        loadCryptoData();
    }

    private void loadCryptoData() {
        ObservableList<Crypto> cryptos = FXCollections.observableArrayList();

        try {
            Map<String, Double> prices = excelReader.readPrices("./Files/ActionCrypto.xlsx", "Cryptocurrencies");
            prices.forEach((name, price) -> {
                cryptos.add(new Crypto(name, price));
            });
            cryptoTable.setItems(cryptos);
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
        loadCryptoData();
    }
}
