package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.User;

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
}
