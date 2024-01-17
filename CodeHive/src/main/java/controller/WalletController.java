package controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Parent;
import model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class WalletController {

    @FXML
    private LineChart<String, Number> balanceChart;
    @FXML
    private PieChart assetDistributionChart;
    @FXML
    private TableView<ObjectTableValue> cryptoTable;

    @FXML
    private TableView<ObjectTableValue> actionTable;

    @FXML
    private SplitPane splitPane;
    @FXML
    private Label balanceLabel;

    @FXML
    private Label cashLabel;

    @FXML
    private TableColumn<ObjectTableValue, String> cryptoNameColumn;

    @FXML
    private TableColumn<ObjectTableValue, String> cryptoBalanceColumn;

    @FXML
    private TableColumn<ObjectTableValue, String> cryptoPriceColumn;

    @FXML
    private TableColumn<ObjectTableValue, String> cryptoQuantityColumn;

    @FXML
    private TableColumn<ObjectTableValue, String> stockNameColumn;

    @FXML
    private TableColumn<ObjectTableValue, String> stockBalanceColumn;

    @FXML
    private TableColumn<ObjectTableValue, String> stockPriceColumn;

    @FXML
    private TableColumn<ObjectTableValue, String> stockQuantityColumn;




    private static final double COLLAPSED_POSITION = 0.05;
    private static final double EXPANDED_POSITION = 0.2;
    private static final Duration ANIMATION_DURATION = Duration.millis(300);

    private User currentUser;

    private List<Crypto> cryptoList = new ArrayList<>();

    private List<Action> actionList = new ArrayList<>();

    private ExcelReader excelReader = new ExcelReader();


    @FXML
    public void initialize() {


    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void initComponents(){
        PieChart.Data cash = new PieChart.Data("Cash", this.currentUser.getBalanceCashAsPercentOfTot());
        PieChart.Data stock = new PieChart.Data("Action", this.currentUser.getBalanceStockAsPercentOfTot());
        PieChart.Data crypto = new PieChart.Data("Crypto", this.currentUser.getBalanceCryptoAsPercentOfTot());

        assetDistributionChart.getData().addAll(cash, crypto,stock);



        this.balanceLabel.setText(this.currentUser.getBalanceTot().toString()+"$");
        this.cashLabel.setText(this.currentUser.getBalanceCash().toString()+"$");

        try {
            Map<String, Double> prices = excelReader.readPrices("./Files/ActionCrypto.xlsx", "Cryptocurrencies");
            prices.forEach((name, price) -> {
                cryptoList.add(new Crypto(name, price));
            });

        } catch (IOException e) {
            e.printStackTrace();
        }




        this.currentUser.initCryptoOwned(this.cryptoList);
        this.cryptoNameColumn.setCellValueFactory(new PropertyValueFactory<ObjectTableValue,String>("name"));
        this.cryptoBalanceColumn.setCellValueFactory(new PropertyValueFactory<ObjectTableValue,String>("ownedValue"));
        this.cryptoPriceColumn.setCellValueFactory(new PropertyValueFactory<ObjectTableValue,String>("price"));
        this.cryptoQuantityColumn.setCellValueFactory(new PropertyValueFactory<ObjectTableValue,String>("quantity"));

        ObservableList<ObjectTableValue> listCrypt = FXCollections.observableArrayList();


        this.currentUser.getCryptoOwned().forEach((crypt,value) -> {

            if(value!=null && value!=0){
                var cryptoTableValue = new ObjectTableValue(crypt.getName(),crypt.getPrice(),value);
                listCrypt.add(cryptoTableValue);
            }

        });

        this.cryptoTable.setItems(listCrypt);


        try {
            Map<String, Double> prices = excelReader.readPrices("./Files/ActionCrypto.xlsx", "Stocks");
            prices.forEach((symbol, price) -> {
                actionList.add(new Action(symbol, price));
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        this.currentUser.initActionOwned(this.actionList);
        this.stockNameColumn.setCellValueFactory(new PropertyValueFactory<ObjectTableValue,String>("name"));
        this.stockBalanceColumn.setCellValueFactory(new PropertyValueFactory<ObjectTableValue,String>("ownedValue"));
        this.stockPriceColumn.setCellValueFactory(new PropertyValueFactory<ObjectTableValue,String>("price"));
        this.stockQuantityColumn.setCellValueFactory(new PropertyValueFactory<ObjectTableValue,String>("quantity"));

        ObservableList<ObjectTableValue> listAction = FXCollections.observableArrayList();


        this.currentUser.getActionOwned().forEach((action,value) -> {

            if(value!=null && value!=0){
                var actionTableValue = new ObjectTableValue(action.getSymbol(),action.getPrice(),value);
                listAction.add(actionTableValue);
            }

        });

        this.actionTable.setItems(listAction);

    }

    @FXML
    public void handleMenuItem1(ActionEvent actionEvent) {
        // Code pour le menu item 1
    }

    // ... Les autres méthodes pour les items du menu ...

    @FXML
    public void handleMenuMouseEntered(MouseEvent mouseEvent) {
        animateDividerPosition(EXPANDED_POSITION);
    }

    @FXML
    public void handleMenuMouseExited(MouseEvent mouseEvent) {
        animateDividerPosition(COLLAPSED_POSITION);
    }

    @FXML
    public void handleMenuButtonClick(ActionEvent actionEvent) {
        toggleMenu();
    }

    @FXML
    public void handleCloseButtonClick(ActionEvent actionEvent) {
        toggleMenu();
    }

    private void animateDividerPosition(double position) {
        if (this.splitPane != null) {
            double[] dividerPositions = this.splitPane.getDividerPositions();
            if (dividerPositions.length > 0) {
                double currentPos = dividerPositions[0];
                KeyFrame start = new KeyFrame(Duration.ZERO, new KeyValue(splitPane.getDividers().get(0).positionProperty(), currentPos));
                KeyFrame end = new KeyFrame(ANIMATION_DURATION, new KeyValue(splitPane.getDividers().get(0).positionProperty(), position));
                Timeline timeline = new Timeline(start, end);
                timeline.play();
            } else {
                // Gérer le cas où il n'y a pas de diviseur dans le SplitPane
            }
        } else {
            // Gérer le cas où splitPane est null
        }
    }

    private void toggleMenu() {
        double currentPos = splitPane.getDividerPositions()[0];
        if (currentPos == EXPANDED_POSITION) {
            animateDividerPosition(COLLAPSED_POSITION);
        } else {
            animateDividerPosition(EXPANDED_POSITION);
        }
    }

    @FXML
    private void handleMenuItem2() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Transaction.fxml"));
            Parent transactionRoot = loader.load();
            TransactionController transactionController = loader.getController();
            transactionController.setCurrentUser(currentUser); // Assurez-vous d'avoir cette méthode dans TransactionController

            Scene transactionScene = new Scene(transactionRoot);
            Stage transactionStage = (Stage) splitPane.getScene().getWindow(); // Utilisez la fenêtre actuelle ou une nouvelle si nécessaire
            transactionStage.setScene(transactionScene);
            transactionStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void handleMenuItem3(ActionEvent actionEvent) {
        // Chargement et affichage du tableau d'actions
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Action.fxml")); // Assurez-vous que le chemin est correct
            Parent actionView = loader.load();

            // Configurez le contrôleur ActionController si nécessaire
            ActionController actionController = loader.getController();
            // actionController.set... // Configurez avec les données nécessaires
            actionController.setCurrentUser(this.currentUser);

            Scene actionScene = new Scene(actionView);
            Stage stage = (Stage) splitPane.getScene().getWindow();
            stage.setScene(actionScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gestion des erreurs
        }
    }

    @FXML
    public void handleMenuItem4(ActionEvent actionEvent) {
        // Chargement et affichage du tableau de cryptomonnaies
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Crypto.fxml")); // Assurez-vous que le chemin est correct
            Parent cryptoView = loader.load();

            // Configurez le contrôleur CryptoController si nécessaire
            CryptoController cryptoController = loader.getController();
            cryptoController.setCurrentUser(this.currentUser);
            cryptoController.initCryptoList(this.cryptoList);
            // cryptoController.set... // Configurez avec les données nécessaires

            Scene cryptoScene = new Scene(cryptoView);
            Stage stage = (Stage) splitPane.getScene().getWindow();
            stage.setScene(cryptoScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gestion des erreurs
        }
    }

    public void handleMenuItem5(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Recharge_Cash.fxml")); // Assurez-vous que le chemin est correct
            Parent cryptoView = loader.load();
            RechargeController rechargeController = loader.getController();
            rechargeController.setCurrentUser(this.currentUser);
            Scene rechargeScene = new Scene(cryptoView);
            Stage stage = (Stage) splitPane.getScene().getWindow();
            stage.setScene(rechargeScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
