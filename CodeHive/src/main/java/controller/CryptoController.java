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
import model.*;

import java.io.IOException;
import java.util.List;
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

    private List<Crypto> cryptoList;

    private User currentUser;


    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));




    }

    public void initCryptoList(List<Crypto> cryptoList){
        this.cryptoList = cryptoList;
        this.setCryptoData(this.cryptoList);

        ObservableList<Crypto> crypto = FXCollections.observableArrayList();
        crypto.addAll(cryptoList);
        this.cryptoTable.setItems(crypto);
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void setCryptoData(List<Crypto> cryptoList){
        ObservableList<Crypto> cryptos = FXCollections.observableArrayList();
        cryptoList.stream().map(cryptos::add);
        cryptoTable.setItems(cryptos);
    }

    @FXML
    protected void handleBackButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Wallet.fxml")); // Mettez à jour avec le chemin correct
            Parent walletRoot = loader.load();

            // Configurez le contrôleur WalletController si nécessaire
            WalletController walletController = loader.getController();
            walletController.setCurrentUser(currentUser);
            walletController.initComponents();

            // Chargez la vue dans la scène actuelle ou une nouvelle, selon vos besoins
            Scene walletScene = new Scene(walletRoot);
            Stage stage = (Stage) cryptoTable.getScene().getWindow(); // Récupérez la fenêtre actuelle
            stage.setScene(walletScene);
        } catch (IOException e) {
            e.printStackTrace();
            // Gérez l'exception comme vous le jugez approprié
        }
    }



    @FXML
    protected void handleRefreshButtonAction(ActionEvent event) {
        startupManager.initializeApplicationData();
        setCryptoData(this.cryptoList);
    }
}
