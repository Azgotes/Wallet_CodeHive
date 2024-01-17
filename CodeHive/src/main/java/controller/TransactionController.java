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
    private User currentUser;

    private static final Set<String> knownCryptocurrencies = Set.of("bitcoin", "ethereum", "ripple" /* autres crypto-monnaies */);
    private static final Set<String> knownStocks = Set.of("AAPL", "MSFT", "GOOGL" /* autres symboles d'actions */);

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
}
