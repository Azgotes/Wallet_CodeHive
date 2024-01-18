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
    // Déclaration des composants d'interface utilisateur annotés avec @FXML.
    @FXML private TextField txtAssetName;
    @FXML private TextField txtAmount;
    @FXML private Label lblStatus;

    // Instance de TransactionManager pour gérer les transactions.
    private TransactionManager transactionManager = new TransactionManager();

    // Variable pour stocker l'utilisateur actuellement connecté.
    private User currentUser;

    // Ensembles de noms connus pour les cryptomonnaies et les actions.
    private static final Set<String> knownCryptocurrencies = Set.of("bitcoin", "ethereum", "ripple","staked-ether","tether","cardano","binancecoin","solana","usd-coin","avalanche-2"/* autres crypto-monnaies */);
    private static final Set<String> knownStocks = Set.of("AAPL", "MSFT", "GOOGL","NFLX","NVDA","TSLA","INTC","AMD","AMZN" /* autres symboles d'actions */);

    // Méthode pour gérer l'action d'achat.
    @FXML
    private void handleBuy() {
        try {
            // Récupération du nom de l'actif et du montant.
            String assetName = txtAssetName.getText();
            double cashAmount = Double.parseDouble(txtAmount.getText());

            // Détermination du type d'actif.
            AssetType assetType = determineAssetType(assetName);
            if (currentUser != null && assetType != null) {
                // Exécution de la transaction d'achat.
                boolean success = transactionManager.executeTransaction(currentUser.getUsername(),
                        assetName, cashAmount, true, assetType, this.currentUser);
                lblStatus.setText(success ? "Purchase successful." : "Purchase failed.");
            } else {
                lblStatus.setText("Transaction failed. Check asset name and type.");
            }
        } catch (NumberFormatException e) {
            lblStatus.setText("Invalid amount format.");
        }
    }

    // Méthode pour gérer l'action de vente.
    @FXML
    private void handleSell() {
        try {
            // Même logique que pour l'achat, mais pour la vente.
            String assetName = txtAssetName.getText();
            double cashAmount = Double.parseDouble(txtAmount.getText());
            AssetType assetType = determineAssetType(assetName);
            if (currentUser != null && assetType != null) {
                boolean success = transactionManager.executeTransaction(currentUser.getUsername(),
                        assetName, cashAmount, false, assetType, this.currentUser);
                lblStatus.setText(success ? "Sale successful." : "Sale failed.");
            } else {
                lblStatus.setText("Transaction failed. Check asset name and type.");
            }
        } catch (NumberFormatException e) {
            lblStatus.setText("Invalid amount format.");
        }
    }

    // Méthode privée pour déterminer le type d'actif.
    private AssetType determineAssetType(String assetName) {
        // Vérifie si l'actif est une cryptomonnaie ou une action.
        if (knownCryptocurrencies.contains(assetName.toLowerCase())) {
            return AssetType.CRYPTO;
        } else if (knownStocks.contains(assetName.toUpperCase())) {
            return AssetType.STOCK;
        } else {
            return null; // ou lever une exception
        }
    }

    // Méthode pour définir l'utilisateur actuel.
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    // Méthode pour gérer le retour à la vue précédente.
    @FXML
    private void handleBack() {
        try {
            // Chargement de la vue Wallet en utilisant FXMLLoader.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Wallet.fxml"));
            Parent walletRoot = loader.load();

            WalletController walletController = loader.getController();
            walletController.setCurrentUser(currentUser);
            walletController.initComponents();

            // Affichage de la vue Wallet dans la scène actuelle.
            Scene walletScene = new Scene(walletRoot);
            Stage stage = (Stage) txtAssetName.getScene().getWindow(); // Récupération de la fenêtre actuelle.
            stage.setScene(walletScene);
        } catch (IOException e) {
            e.printStackTrace();
            // Gestion des exceptions liées au chargement de la vue.
        }
    }
}

