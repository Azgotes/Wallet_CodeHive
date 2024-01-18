package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import model.StartupManager;

import java.io.IOException;

public class LoginController {
    // Définition des composants d'interface utilisateur annotés avec @FXML.
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblStatus;

    // Instance de User pour gérer les services liés à l'utilisateur.
    private User userService = new User();

    // Instance de StartupManager, probablement utilisée pour l'initialisation de l'application.
    private StartupManager startupManager = new StartupManager(); // Ajoutez ceci

    // Méthode pour gérer l'action de connexion.
    @FXML
    private void handleLogin(ActionEvent event) {
        // Récupération du nom d'utilisateur et du mot de passe à partir des champs de texte.
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        // Vérification des identifiants de l'utilisateur.
        if (userService.authenticate(username, password)) {
            try {
                // Si la vérification réussit, chargez la vue Wallet.
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Wallet.fxml"));
                Parent walletView = loader.load();

                // Obtenez le contrôleur de la vue Wallet et définissez l'utilisateur actuel.
                WalletController walletController = loader.getController();
                walletController.setCurrentUser(userService); // userService contient maintenant l'utilisateur authentifié

                walletController.initComponents();
                // Affichez la vue Wallet dans la fenêtre actuelle.
                Scene walletScene = new Scene(walletView);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(walletScene);
                stage.setTitle("My Wallet");
                stage.show();
            } catch (IOException e) {
                lblStatus.setText("Error loading wallet view.");
                e.printStackTrace();
            }
        } else {
            // Si l'authentification échoue, affichez un message d'erreur.
            lblStatus.setText("Login failed.");
        }
    }

    // Méthode pour gérer l'action d'inscription.
    @FXML
    private void handleSignUp(ActionEvent event) {
        try {
            // Chargez la vue d'inscription.
            Parent signUpView = FXMLLoader.load(getClass().getResource("/fxml/SignUp.fxml"));

            // Affichez la vue d'inscription dans la fenêtre actuelle.
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(signUpView));
            stage.show();
        } catch (IOException e) {
            lblStatus.setText("Error loading sign up view.");
            e.printStackTrace();
        }
    }
}

