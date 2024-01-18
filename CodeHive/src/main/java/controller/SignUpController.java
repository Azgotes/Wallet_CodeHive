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

import java.io.IOException;

public class SignUpController {
    // Déclaration des composants de l'interface utilisateur annotés avec @FXML.
    @FXML private TextField txtNewUsername;
    @FXML private PasswordField txtNewPassword;
    @FXML private TextField txtNewEmail;
    @FXML private Label lblNewUserStatus;

    // Instance de User pour gérer les services liés à l'inscription de l'utilisateur.
    private User userService = new User();

    // Méthode pour gérer l'action d'inscription.
    @FXML
    private void handleSignUp(ActionEvent event) {
        // Récupération des valeurs entrées par l'utilisateur.
        String newUsername = txtNewUsername.getText();
        String newPassword = txtNewPassword.getText();
        String email = txtNewEmail.getText(); // Récupération de l'email

        // Tentative d'inscription de l'utilisateur avec les informations fournies.
        if (userService.registerUser(newUsername, newPassword, email)) {
            // En cas de succès, afficher un message de réussite.
            lblNewUserStatus.setText("User registered successfully.");
        } else {
            // En cas d'échec, afficher un message d'erreur.
            lblNewUserStatus.setText("Registration failed. User may already exist.");
        }
    }

    // Méthode pour gérer l'action du bouton de retour.
    @FXML
    private void handleReturnButton(ActionEvent event) {
        // Redirige l'utilisateur vers la page de connexion.
        redirectToLoginPage(event);
    }

    // Méthode privée pour rediriger vers la page de connexion.
    private void redirectToLoginPage(ActionEvent event) {
        try {
            // Chargement de la vue de connexion.
            Parent loginView = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
            Scene loginScene = new Scene(loginView);

            // Récupération de la scène actuelle et mise à jour avec la vue de connexion.
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gestion des exceptions liées au chargement de la vue.
        }
    }
}
