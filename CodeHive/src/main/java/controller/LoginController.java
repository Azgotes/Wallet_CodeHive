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

public class LoginController {
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblStatus;

    private User userService = new User();

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (userService.authenticate(username, password)) {
            try {
                // Chargez la vue Wallet après une connexion réussie
                Parent walletView = FXMLLoader.load(getClass().getResource("/fxml/Wallet.fxml"));
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
            lblStatus.setText("Login failed.");
        }
    }


    @FXML
    private void handleSignUp(ActionEvent event) {
        try {
            Parent signUpView = FXMLLoader.load(getClass().getResource("/fxml/SignUp.fxml"));

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(signUpView));
            stage.show();
        } catch (IOException e) {
            lblStatus.setText("Error loading sign up view.");
            e.printStackTrace();
        }
    }


}
