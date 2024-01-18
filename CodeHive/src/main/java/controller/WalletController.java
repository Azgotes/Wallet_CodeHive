package controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Parent;
import javafx.scene.Scene;
import model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    private void initialize() {
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        initComponents();
    }

    public void initComponents() {
        initCharts();
        initCryptoTable();
        initActionTable();
    }

    private void initCharts() {
        PieChart.Data cash = new PieChart.Data("Cash", this.currentUser.getBalanceCashAsPercentOfTot());
        PieChart.Data stock = new PieChart.Data("Action", this.currentUser.getBalanceStockAsPercentOfTot());
        PieChart.Data crypto = new PieChart.Data("Crypto", this.currentUser.getBalanceCryptoAsPercentOfTot());

        assetDistributionChart.getData().addAll(cash, crypto, stock);

        balanceLabel.setText(this.currentUser.getBalanceTot().toString() + "$");
        cashLabel.setText(this.currentUser.getBalanceCash().toString() + "$");
    }

    private void initCryptoTable() {
        try {
            Map<String, Double> prices = excelReader.readPrices("./Files/ActionCrypto.xlsx", "Cryptocurrencies");
            prices.forEach((name, price) -> cryptoList.add(new Crypto(name, price)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        currentUser.initCryptoOwned(cryptoList);
        cryptoNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        cryptoBalanceColumn.setCellValueFactory(new PropertyValueFactory<>("ownedValue"));
        cryptoPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        cryptoQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        ObservableList<ObjectTableValue> listCrypt = FXCollections.observableArrayList();

        currentUser.getCryptoOwned().forEach((crypt, value) -> {
            if (value != null && value != 0) {
                ObjectTableValue cryptoTableValue = new ObjectTableValue(crypt.getName(), crypt.getPrice(), value);
                listCrypt.add(cryptoTableValue);
            }
        });

        cryptoTable.setItems(listCrypt);
    }

    private void initActionTable() {
        try {
            Map<String, Double> prices = excelReader.readPrices("./Files/ActionCrypto.xlsx", "Stocks");
            prices.forEach((symbol, price) -> actionList.add(new Action(symbol, price)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        currentUser.initActionOwned(actionList);
        stockNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        stockBalanceColumn.setCellValueFactory(new PropertyValueFactory<>("ownedValue"));
        stockPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        stockQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        ObservableList<ObjectTableValue> listAction = FXCollections.observableArrayList();

        currentUser.getActionOwned().forEach((action, value) -> {
            if (value != null && value != 0) {
                ObjectTableValue actionTableValue = new ObjectTableValue(action.getSymbol(), action.getPrice(), value);
                listAction.add(actionTableValue);
            }
        });

        actionTable.setItems(listAction);
    }

    @FXML
    private void handleMenuMouseEntered(MouseEvent mouseEvent) {
        animateDividerPosition(EXPANDED_POSITION);
    }

    @FXML
    private void handleMenuMouseExited(MouseEvent mouseEvent) {
        animateDividerPosition(COLLAPSED_POSITION);
    }

    @FXML
    private void handleMenuButtonClick(ActionEvent actionEvent) {
        toggleMenu();
    }

    @FXML
    private void handleCloseButtonClick(ActionEvent actionEvent) {
        toggleMenu();
    }

    private void animateDividerPosition(double position) {
        if (splitPane != null) {
            double[] dividerPositions = splitPane.getDividerPositions();
            if (dividerPositions.length > 0) {
                double currentPos = dividerPositions[0];
                KeyFrame start = new KeyFrame(Duration.ZERO, new KeyValue(splitPane.getDividers().get(0).positionProperty(), currentPos));
                KeyFrame end = new KeyFrame(ANIMATION_DURATION, new KeyValue(splitPane.getDividers().get(0).positionProperty(), position));
                Timeline timeline = new Timeline(start, end);
                timeline.play();
            } else {
            }
        } else {
        }
    }

    private void toggleMenu() {
        if (splitPane != null) {
            double currentPos = splitPane.getDividerPositions()[0];
            animateDividerPosition(currentPos == EXPANDED_POSITION ? COLLAPSED_POSITION : EXPANDED_POSITION);
        }
    }

    @FXML
    private void handleMenuItem2() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Transaction.fxml"));
            Parent transactionRoot = loader.load();
            TransactionController transactionController = loader.getController();
            transactionController.setCurrentUser(currentUser);
            Scene transactionScene = new Scene(transactionRoot);
            Stage transactionStage = (Stage) splitPane.getScene().getWindow();
            transactionStage.setScene(transactionScene);
            transactionStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMenuItem3(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Action.fxml"));
            Parent actionView = loader.load();
            ActionController actionController = loader.getController();
            actionController.setCurrentUser(currentUser);
            Scene actionScene = new Scene(actionView);
            Stage stage = (Stage) splitPane.getScene().getWindow();
            stage.setScene(actionScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMenuItem4(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Crypto.fxml"));
            Parent cryptoView = loader.load();
            CryptoController cryptoController = loader.getController();
            cryptoController.setCurrentUser(currentUser);
            cryptoController.initCryptoList(cryptoList);
            Scene cryptoScene = new Scene(cryptoView);
            Stage stage = (Stage) splitPane.getScene().getWindow();
            stage.setScene(cryptoScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMenuItem5(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Recharge_Cash.fxml"));
            Parent rechargeView = loader.load();
            RechargeController rechargeController = loader.getController();
            rechargeController.setCurrentUser(currentUser);
            Scene rechargeScene = new Scene(rechargeView);
            Stage stage = (Stage) splitPane.getScene().getWindow();
            stage.setScene(rechargeScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleLogout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Êtes-vous sûr de vous déconnecter ?");
        alert.setContentText("Cliquez sur OK pour confirmer.");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Parent loginView = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
                Scene loginScene = new Scene(loginView);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                stage.setScene(loginScene);
                stage.setTitle("Login");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}