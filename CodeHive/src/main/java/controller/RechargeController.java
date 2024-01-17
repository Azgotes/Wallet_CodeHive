package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;


public class RechargeController {

    @FXML private TextField txtAmount;

    private User currentUser;

    public void handleRecharge(ActionEvent actionEvent) {
        double cashAmount = Double.parseDouble(txtAmount.getText());
        this.currentUser.setBalanceCash(this.currentUser.getBalanceCash()+cashAmount);
        handleBack(null);
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @FXML
    public void handleBack(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Wallet.fxml")); // Mettez à jour avec le chemin correct
            Parent walletRoot = loader.load();

            // Configurez le contrôleur WalletController si nécessaire
            WalletController walletController = loader.getController();
            walletController.setCurrentUser(currentUser);
            walletController.initComponents();


            // Chargez la vue dans la scène actuelle ou une nouvelle, selon vos besoins
            Scene walletScene = new Scene(walletRoot);
            Stage stage = (Stage) txtAmount.getScene().getWindow(); // Récupérez la fenêtre actuelle
            stage.setScene(walletScene);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
