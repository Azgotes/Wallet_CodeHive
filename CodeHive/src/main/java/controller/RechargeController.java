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

    // Champ de texte pour entrer le montant du rechargement, annoté avec @FXML pour le lier à l'interface utilisateur.
    @FXML private TextField txtAmount;

    // Variable pour stocker l'utilisateur actuellement connecté.
    private User currentUser;

    // Méthode pour gérer l'action de rechargement.
    public void handleRecharge(ActionEvent actionEvent) {
        // Conversion de la valeur entrée dans le champ de texte en un double.
        double cashAmount = Double.parseDouble(txtAmount.getText());

        // Mise à jour du solde de l'utilisateur avec le montant ajouté.
        this.currentUser.setBalanceCash(this.currentUser.getBalanceCash() + cashAmount);

        // Retour à la vue précédente après le rechargement.
        handleBack(null);
    }

    // Méthode pour définir l'utilisateur actuel.
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    // Méthode pour gérer le retour à la vue précédente.
    @FXML
    public void handleBack(ActionEvent actionEvent) {
        try {
            // Chargement de la vue Wallet en utilisant FXMLLoader.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Wallet.fxml")); // Assurez-vous que le chemin est correct.
            Parent walletRoot = loader.load();

            // Configuration du contrôleur WalletController avec l'utilisateur actuel.
            WalletController walletController = loader.getController();
            walletController.setCurrentUser(currentUser);
            walletController.initComponents();

            // Affichage de la vue Wallet dans la scène actuelle.
            Scene walletScene = new Scene(walletRoot);
            Stage stage = (Stage) txtAmount.getScene().getWindow(); // Récupération de la fenêtre actuelle.
            stage.setScene(walletScene);
        } catch (IOException e) {
            e.printStackTrace();
            // Gestion des exceptions liées au chargement de la vue.
        }
    }
}

