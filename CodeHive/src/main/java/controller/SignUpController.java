package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;

public class SignUpController {
    @FXML
    private TextField txtNewFirstName;
    @FXML
    private TextField txtNewLastName;
    @FXML
    private TextField txtNewUsername;
    @FXML
    private PasswordField txtNewPassword;
    @FXML
    private TextField txtNewEmail;
    @FXML
    private Label lblNewUserStatus;

    private User userService = new User();

    @FXML
    private void handleSignUp(ActionEvent event) {
        String newUsername = txtNewUsername.getText();
        String newPassword = txtNewPassword.getText();
        String email = txtNewEmail.getText();


        if (!isValidPassword(newPassword)) {
            lblNewUserStatus.setText("Password must contain at least one uppercase letter, one lowercase letter, and one digit.");
            return;
        }

        if (!isValidEmail(email)) {
            lblNewUserStatus.setText("Invalid email address.");
            return;
        }

        if (userService.registerUser(newUsername, newPassword, email)) {
            lblNewUserStatus.setText("User registered successfully.");
        } else {
            lblNewUserStatus.setText("Registration failed. User may already exist.");
        }
    }


    @FXML
    private void handleReturnButton(ActionEvent event) {
        redirectToLoginPage(event);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
        return email.matches(emailRegex);
    }

    private boolean isValidPassword(String password) {
        return password.matches("(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+");
    }

    @FXML
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
