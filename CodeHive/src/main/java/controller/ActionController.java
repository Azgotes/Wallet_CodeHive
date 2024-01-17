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
import model.User;

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

    private User currentUser;


    private final ExcelReader excelReader = new ExcelReader();

    @FXML
    public void initialize() {
        symbolColumn.setCellValueFactory(new PropertyValueFactory<>("symbol"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        loadActionData();
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Wallet.fxml")); // Mettez à jour avec le chemin correct
            Parent walletRoot = loader.load();

            // Configurez le contrôleur WalletController si nécessaire
            WalletController walletController = loader.getController();
            walletController.setCurrentUser(currentUser);
            walletController.initComponents();

            // Chargez la vue dans la scène actuelle ou une nouvelle, selon vos besoins
            Scene walletScene = new Scene(walletRoot);
            Stage stage = (Stage) actionTable.getScene().getWindow(); // Récupérez la fenêtre actuelle
            stage.setScene(walletScene);
        } catch (IOException e) {
            e.printStackTrace();
            // Gérez l'exception comme vous le jugez approprié
        }
    }


    @FXML
    protected void handleRefreshButtonAction(ActionEvent event) {
        startupManager.initializeApplicationData();
        loadActionData();
    }
}
