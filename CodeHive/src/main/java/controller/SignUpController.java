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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        String newUsername = txtNewUsername.getText();
        String newPassword = txtNewPassword.getText();
        String email = txtNewEmail.getText();

        // Vérifier que le mot de passe a au moins une majuscule, une minuscule et un chiffre
        if (!isValidPassword(newPassword)) {
            lblNewUserStatus.setText("Password must contain at least one uppercase letter, one lowercase letter, and one digit.");
            return;
        }

        // Vérifier que l'email est correct
        if (!isValidEmail(email)) {
            lblNewUserStatus.setText("Invalid email address.");
            return;
        }

        // Appel de la méthode registerUser avec le nom, prénom, etc.
        if (userService.registerUser(newUsername, newPassword, email)) {
            lblNewUserStatus.setText("User registered successfully.");
        } else {
            lblNewUserStatus.setText("Registration failed. User may already exist.");
        }
    }

    private boolean isValidPassword(String password) {
        // Vérifier la présence d'au moins une majuscule, une minuscule et un chiffre
        boolean containsUppercase = false;
        boolean containsLowercase = false;
        boolean containsDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                containsUppercase = true;
            } else if (Character.isLowerCase(c)) {
                containsLowercase = true;
            } else if (Character.isDigit(c)) {
                containsDigit = true;
            }
        }

        // Utiliser une regex pour vérifier d'autres conditions (par exemple, la longueur minimale)
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        return containsUppercase && containsLowercase && containsDigit && matcher.matches();
    }

    private boolean isValidEmail(String email) {
        // Utiliser une expression régulière pour valider l'email
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
        return email.matches(emailRegex);
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
