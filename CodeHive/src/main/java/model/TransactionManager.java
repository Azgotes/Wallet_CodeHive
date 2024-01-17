package model;

import java.io.IOException;
import java.util.Map;

public class TransactionManager {
    private ExcelReader excelReader = new ExcelReader();
    private ExcelWriter excelWriter = new ExcelWriter();
    private CoinGeckoService coinGeckoService = new CoinGeckoService();
    private AlphaVantageService alphaVantageService = new AlphaVantageService();

    public boolean executeTransaction(String username, String assetName, double amount, boolean isBuying, AssetType assetType) {
        try {
            double price = getCurrentPrice(assetName, assetType);
            Map<String, Double> userAssets = excelReader.readUserAssets(username, "./Files/users.xlsx");
            double balance = userAssets.getOrDefault("balance", 0.0);
            double assetQuantity = userAssets.getOrDefault(assetName, 0.0);
            double transactionCost = price * amount;

            if (isBuying) {
                if (balance < transactionCost) {
                    System.out.println("Fonds insuffisants pour l'achat.");
                    return false;
                }
                balance -= transactionCost;
                assetQuantity += amount; // Augmentez la quantité de l'actif
            } else {
                if (assetQuantity < amount) {
                    System.out.println("Quantité d'actif insuffisante pour la vente.");
                    return false;
                }
                balance += transactionCost;
                assetQuantity -= amount; // Diminuez la quantité de l'actif
            }

            // Mise à jour des actifs et du solde de l'utilisateur
            userAssets.put("balance", balance);
            userAssets.put(assetName, assetQuantity);

            // Enregistrez les nouvelles valeurs d'actifs dans le fichier Excel.
            excelWriter.updateUserAssets(username, userAssets, "./Files/users.xlsx");

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }



    private double getCurrentPrice(String assetName, AssetType assetType) {
        try {
            double price;
            if (assetType == AssetType.CRYPTO) {
                price = coinGeckoService.getCurrentCryptoPrice(assetName);
            } else {
                price = alphaVantageService.getCurrentStockPrice(assetName);
            }
            return price != -1 ? price : excelReader.readPrice(assetName, assetType, "./Files/ActionCrypto.xlsx");
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

}
