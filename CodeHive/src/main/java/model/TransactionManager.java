package model;

import java.io.IOException;
import java.util.Map;

public class TransactionManager {
    private ExcelReader excelReader = new ExcelReader();
    private ExcelWriter excelWriter = new ExcelWriter();
    private CoinGeckoService coinGeckoService = new CoinGeckoService();
    private AlphaVantageService alphaVantageService = new AlphaVantageService();

    public boolean executeTransaction(String username, String assetName, double cashValue, boolean isBuying, AssetType assetType) {
        try {
            double assetPrice = getCurrentPrice(assetName, assetType);
            double assetAmount = cashValue / assetPrice;

            Map<String, Double> userAssets = excelReader.readUserAssets(username, "./Files/users.xlsx");
            double balanceTotal = userAssets.getOrDefault("balance_total", 0.0);
            double balanceCrypto = userAssets.getOrDefault("balance_crypto", 0.0);
            double balanceStocks = userAssets.getOrDefault("balance_stocks", 0.0);
            double balanceCash = userAssets.getOrDefault("balance_cash", 0.0);

            if (isBuying) {
                if (balanceCash < cashValue) {
                    System.out.println("Insufficient funds for purchase.");
                    return false;
                }
                balanceCash -= cashValue;
                assetAmount += userAssets.getOrDefault(assetName, 0.0);
                if (assetType == AssetType.CRYPTO) {
                    balanceCrypto += cashValue;
                } else {
                    balanceStocks += cashValue;
                }
            } else {
                double currentAssetAmount = userAssets.getOrDefault(assetName, 0.0);
                double assetValueToSell = assetAmount * assetPrice; // Valeur monétaire de la quantité à vendre

                System.out.println("Attempting to sell: " + assetAmount + " of " + assetName);
                System.out.println("Current asset amount: " + currentAssetAmount);
                System.out.println("Asset price: " + assetPrice);
                System.out.println("Asset value to sell: " + assetValueToSell);
                System.out.println("User's cash before sale: " + balanceCash);

                if (currentAssetAmount < assetAmount) {
                    System.out.println("Insufficient asset quantity for sale.");
                    return false;
                }

                balanceCash += assetValueToSell;
                assetAmount = currentAssetAmount - assetAmount;

                System.out.println("User's cash after sale: " + balanceCash);
                System.out.println("New asset amount: " + assetAmount);

                if (assetType == AssetType.CRYPTO) {
                    balanceCrypto -= assetValueToSell; // Ici, on déduit la valeur monétaire, pas la quantité
                } else {
                    balanceStocks -= assetValueToSell; // De même pour les stocks
                }
            }

            balanceTotal = balanceCash + balanceCrypto + balanceStocks;

            userAssets.put("balance_total", balanceTotal);
            userAssets.put("balance_crypto", balanceCrypto);
            userAssets.put("balance_stocks", balanceStocks);
            userAssets.put("balance_cash", balanceCash);
            userAssets.put(assetName, assetAmount);

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
