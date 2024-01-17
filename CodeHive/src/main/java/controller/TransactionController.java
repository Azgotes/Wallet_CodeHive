package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import model.TransactionManager;
import model.User;
import model.AssetType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Set;

public class TransactionController {
    @FXML private TextField txtAssetName;
    @FXML private TextField txtAmount;
    @FXML private Label lblStatus;

    private TransactionManager transactionManager = new TransactionManager();
    private User currentUser;

    private static final Set<String> knownCryptocurrencies = Set.of("bitcoin", "ethereum", "ripple","staked-ether","tether","cardano","binancecoin","solana","usd-coin","avalanche-2"/* autres crypto-monnaies */);
    private static final Set<String> knownStocks = Set.of("AAPL", "MSFT", "GOOGL","NFLX","NVDA","TSLA","INTC","AMD","AMZN" /* autres symboles d'actions */);

    @FXML
    private void handleBuy() {
        try {
            String assetName = txtAssetName.getText();
            double cashAmount = Double.parseDouble(txtAmount.getText());
            AssetType assetType = determineAssetType(assetName);
            if (currentUser != null && assetType != null) {
                boolean success = transactionManager.executeTransaction(currentUser.getUsername(), assetName, cashAmount, true, assetType);
                lblStatus.setText(success ? "Purchase successful." : "Purchase failed.");
            } else {
                lblStatus.setText("Transaction failed. Check asset name and type.");
            }
        } catch (NumberFormatException e) {
            lblStatus.setText("Invalid amount format.");
        }
    }

    @FXML
    private void handleSell() {
        try {
            String assetName = txtAssetName.getText();
            double cashAmount = Double.parseDouble(txtAmount.getText());
            AssetType assetType = determineAssetType(assetName);
            if (currentUser != null && assetType != null) {
                boolean success = transactionManager.executeTransaction(currentUser.getUsername(), assetName, cashAmount, false, assetType);
                lblStatus.setText(success ? "Sale successful." : "Sale failed.");
            } else {
                lblStatus.setText("Transaction failed. Check asset name and type.");
            }
        } catch (NumberFormatException e) {
            lblStatus.setText("Invalid amount format.");
        }
    }

    private AssetType determineAssetType(String assetName) {
        if (knownCryptocurrencies.contains(assetName.toLowerCase())) {
            return AssetType.CRYPTO;
        } else if (knownStocks.contains(assetName.toUpperCase())) {
            return AssetType.STOCK;
        } else {
            return null; // ou lever une exception
        }
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Wallet.fxml")); // Mettez à jour avec le chemin correct
            Parent walletRoot = loader.load();

            // Configurez le contrôleur WalletController si nécessaire
            WalletController walletController = loader.getController();
            walletController.setCurrentUser(currentUser);

            // Chargez la vue dans la scène actuelle ou une nouvelle, selon vos besoins
            Scene walletScene = new Scene(walletRoot);
            Stage stage = (Stage) txtAssetName.getScene().getWindow(); // Récupérez la fenêtre actuelle
            stage.setScene(walletScene);
        } catch (IOException e) {
            e.printStackTrace();
            // Gérez l'exception comme vous le jugez approprié
        }
    }
}
