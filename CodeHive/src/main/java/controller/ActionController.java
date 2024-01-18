package controller;

// Importation des bibliothèques nécessaires.
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
import model.User;

import java.io.IOException;
import java.util.Map;

public class ActionController {

    // Déclaration des composants de l'interface utilisateur.
    @FXML
    private TableView<Action> actionTable;
    @FXML
    private TableColumn<Action, String> symbolColumn;
    @FXML
    private TableColumn<Action, Double> priceColumn;

    // Instances de StartupManager et ExcelReader pour la gestion des données.
    private final StartupManager startupManager = new StartupManager();
    private User currentUser;
    private final ExcelReader excelReader = new ExcelReader();

    // Méthode d'initialisation appelée pour configurer les colonnes du tableau.
    @FXML
    public void initialize() {
        // Configuration des colonnes pour afficher les symboles et les prix des actions.
        symbolColumn.setCellValueFactory(new PropertyValueFactory<>("symbol"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Chargement des données des actions.
        loadActionData();
    }

    // Méthode pour définir l'utilisateur actuel.
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    // Méthode privée pour charger les données des actions.
    private void loadActionData() {
        // Création d'une liste observable pour les actions.
        ObservableList<Action> actions = FXCollections.observableArrayList();

        try {
            // Lecture des prix depuis un fichier Excel et ajout dans la liste.
            Map<String, Double> prices = excelReader.readPrices("./Files/ActionCrypto.xlsx", "Stocks");
            prices.forEach((symbol, price) -> {
                actions.add(new Action(symbol, price));
            });
            // Définition des actions dans le tableau de l'interface utilisateur.
            actionTable.setItems(actions);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            Stage stage = (Stage) actionTable.getScene().getWindow();
            stage.setScene(walletScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Gestion de l'action du bouton "Actualiser".
    @FXML
    protected void handleRefreshButtonAction(ActionEvent event) {
        // Réinitialisation des données de l'application et rechargement des données des actions.
        startupManager.initializeApplicationData();
        loadActionData();
    }
}
