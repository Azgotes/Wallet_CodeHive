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
            double balance = userAssets.getOrDefault("balance", 100000.0);
            double assetQuantity = userAssets.getOrDefault(assetName, 0.0);

            double transactionCost = price * amount;

            if (isBuying) {
                if (balance < transactionCost) {
                    System.out.println("Insufficient funds.");
                    return false;
                }
                balance -= transactionCost;
                assetQuantity += amount;
            } else {
                if (assetQuantity < amount) {
                    System.out.println("Insufficient asset quantity.");
                    return false;
                }
                balance += transactionCost;
                assetQuantity -= amount;
            }

            userAssets.put("balance", balance);
            userAssets.put(assetName, assetQuantity);
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

    // Other helper methods for ExcelReader and ExcelWriter...
}
