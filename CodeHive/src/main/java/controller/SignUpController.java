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
    @FXML private TextField txtNewUsername;
    @FXML private PasswordField txtNewPassword;
    @FXML private TextField txtEmail; // Nouveau champ d'e-mail
    @FXML private Label lblNewUserStatus;

    private User userService = new User();

    @FXML
    private void handleSignUp(ActionEvent event) {
        String newUsername = txtNewUsername.getText();
        String newPassword = txtNewPassword.getText();
        String email = txtEmail.getText();

        if (validateEmail(email) && userService.registerUser(newUsername, newPassword, email)) {
            // Envoyez un e-mail de confirmation ici (étape supplémentaire)
            lblNewUserStatus.setText("User registered successfully. Check your email for confirmation.");
            // Vous pouvez ajouter du code ici pour passer à la vue de connexion si nécessaire
        } else {
            lblNewUserStatus.setText("Registration failed. User may already exist or email is invalid.");
        }
    }

    @FXML
    private void handleReturnButton(ActionEvent event) {
        redirectToLoginPage(event);
    }

    @FXML
    private void handleCreateEmail(ActionEvent event) {
        // Ajoutez ici le code pour créer un e-mail et l'enregistrer dans votre fichier xlsx
    }

    @FXML
    private void handleViewEmails(ActionEvent event) {
        // Ajoutez ici le code pour afficher les e-mails à partir de votre fichier xlsx
    }

    private void redirectToLoginPage(ActionEvent event) {
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

    private boolean validateEmail(String email) {
        // Ajoutez votre logique de validation d'e-mail ici (regex, etc.)
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }
}
