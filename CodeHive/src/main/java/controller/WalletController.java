package controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Parent;
import model.User;

import java.io.IOException;


public class WalletController {

    @FXML
    private LineChart<String, Number> balanceChart;
    @FXML
    private PieChart assetDistributionChart;
    @FXML
    private TableView<?> cryptoTable;
    @FXML
    private SplitPane splitPane;
    @FXML
    private Button menuButton;
    @FXML
    private VBox menuVBox;
    @FXML
    private VBox balanceVBox;
    @FXML
    private VBox cryptoVBox;
    @FXML
    private VBox actionVBox;
    @FXML
    private Button closeButton;

    private static final double COLLAPSED_POSITION = 0.05;
    private static final double EXPANDED_POSITION = 0.2;
    private static final Duration ANIMATION_DURATION = Duration.millis(300);

    private User currentUser;


    @FXML
    public void initialize() {
        // Initialiser le graphique de solde
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        // Ajouter des données à la série (faux exemple)
        series.getData().add(new XYChart.Data<>("12:57 PM", 32.84));
        balanceChart.getData().add(series);

        // Initialiser le diagramme de répartition des actifs
        PieChart.Data slice1 = new PieChart.Data("Crypto", 86.2);
        PieChart.Data slice2 = new PieChart.Data("Action", 13.8);

        assetDistributionChart.getData().addAll(slice1, slice2);

        // Initialiser le tableau des cryptomonnaies
        // tableau avec des données réelles seront placé ici

        // Initialiser le tableau des actions
        // ...
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
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
    }
}
