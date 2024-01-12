package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import model.TransactionManager;
import model.User;
import model.AssetType;

import java.util.Set;

public class TransactionController {
    @FXML private TextField txtAssetName;
    @FXML private TextField txtAmount;
    @FXML private Label lblStatus;

    private TransactionManager transactionManager = new TransactionManager();
    private User currentUser; // Instance de User

    private static final Set<String> knownCryptocurrencies = Set.of("bitcoin", "ethereum", "ripple" /* autres crypto-monnaies */);
    private static final Set<String> knownStocks = Set.of("AAPL", "MSFT", "GOOGL" /* autres symboles d'actions */);

    @FXML
    private void handleBuy() {
        executeTransaction(true);
    }

    @FXML
    private void handleSell() {
        executeTransaction(false);
    }

    private void executeTransaction(boolean isBuying) {
        String assetName = txtAssetName.getText().toLowerCase();
        double amount;
        try {
            amount = Double.parseDouble(txtAmount.getText());
        } catch (NumberFormatException e) {
            lblStatus.setText("Montant invalide.");
            return;
        }

        AssetType assetType = determineAssetType(assetName);

        if (assetType != null && currentUser != null && transactionManager.executeTransaction(currentUser.getUsername(), assetName, amount, isBuying, assetType)) {
            lblStatus.setText("Transaction réussie");
        } else {
            lblStatus.setText("Échec de la transaction");
        }
    }

    private AssetType determineAssetType(String assetName) {
        if (knownCryptocurrencies.contains(assetName)) {
            return AssetType.CRYPTO;
        } else if (knownStocks.contains(assetName.toUpperCase())) {
            return AssetType.STOCK;
        } else {
            lblStatus.setText("Type d'actif inconnu.");
            return null;
        }
    }

    // Méthode pour définir l'utilisateur actuel
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
}
