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

    // Déclaration des composants de l'interface utilisateur pour le tableau et les colonnes.
    @FXML
    private TableView<Crypto> cryptoTable;
    @FXML
    private TableColumn<Crypto, String> nameColumn;
    @FXML
    private TableColumn<Crypto, Double> priceColumn;

    // Instance de ExcelReader pour la lecture de données.
    private final ExcelReader excelReader = new ExcelReader();

    // Instance de StartupManager pour la gestion du démarrage de l'application.
    private final StartupManager startupManager = new StartupManager();

    // Liste pour stocker les données des cryptomonnaies.
    private List<Crypto> cryptoList;

    // Variable pour stocker l'utilisateur actuel.
    private User currentUser;

    // Méthode d'initialisation appelée pour configurer les colonnes du tableau.
    @FXML
    public void initialize() {
        // Configuration des colonnes pour afficher les noms et les prix des cryptomonnaies.
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    // Méthode pour initialiser la liste des cryptomonnaies et mettre à jour les données dans le tableau.
    public void initCryptoList(List<Crypto> cryptoList){
        this.cryptoList = cryptoList;
        this.setCryptoData(this.cryptoList);

        ObservableList<Crypto> crypto = FXCollections.observableArrayList();
        crypto.addAll(cryptoList);
        this.cryptoTable.setItems(crypto);
    }

    // Méthode pour définir l'utilisateur actuel.
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    // Méthode pour configurer les données des cryptomonnaies dans le tableau.
    public void setCryptoData(List<Crypto> cryptoList){
        ObservableList<Crypto> cryptos = FXCollections.observableArrayList();
        cryptoList.stream().map(cryptos::add);
        cryptoTable.setItems(cryptos);
    }

    // Gestion de l'action du bouton "Retour".
    @FXML
    protected void handleBackButtonAction(ActionEvent event) {
        try {
            // Chargement de l'interface utilisateur du portefeuille et configuration du contrôleur.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Wallet.fxml"));
            Parent walletRoot = loader.load();

            WalletController walletController = loader.getController();
            walletController.setCurrentUser(currentUser);
            walletController.initComponents();

            // Mise à jour de la scène actuelle avec la nouvelle interface utilisateur.
            Scene walletScene = new Scene(walletRoot);
            Stage stage = (Stage) cryptoTable.getScene().getWindow();
            stage.setScene(walletScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Gestion de l'action du bouton "Actualiser".
    @FXML
    protected void handleRefreshButtonAction(ActionEvent event) {
        // Réinitialisation des données de l'application et rechargement des données des cryptomonnaies.
        startupManager.initializeApplicationData();
        setCryptoData(this.cryptoList);
    }
}
