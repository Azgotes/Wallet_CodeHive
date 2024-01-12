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
    @FXML private Label lblNewUserStatus;

    private User userService = new User();

    @FXML
    private void handleSignUp(ActionEvent event) {
        String newUsername = txtNewUsername.getText();
        String newPassword = txtNewPassword.getText();

        if (userService.registerUser(newUsername, newPassword)) {
            lblNewUserStatus.setText("User registered successfully.");
            // Vous pouvez ajouter du code ici pour passer à la vue de connexion si nécessaire
        } else {
            lblNewUserStatus.setText("Registration failed. User may already exist.");
        }
    }

    @FXML
    private void handleReturnButton(ActionEvent event) {
        redirectToLoginPage(event);
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
}
