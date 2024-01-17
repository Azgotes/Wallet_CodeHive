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
                // Utilisez le FXMLLoader pour charger la vue Wallet
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Wallet.fxml"));
                Parent walletView = loader.load();

                // Obtenez le WalletController et définissez l'utilisateur actuel
                WalletController walletController = loader.getController();
                walletController.setCurrentUser(userService); // userService contient maintenant l'utilisateur authentifié

                // Affichez la vue Wallet
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
