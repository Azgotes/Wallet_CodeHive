package model;

import java.io.IOException;
import java.util.Map;

public class TransactionManager {
    // Instances pour lire et écrire dans un fichier Excel et pour accéder aux services API.
    private ExcelReader excelReader = new ExcelReader();
    private ExcelWriter excelWriter = new ExcelWriter();
    private CoinGeckoService coinGeckoService = new CoinGeckoService();
    private AlphaVantageService alphaVantageService = new AlphaVantageService();

    // Méthode pour exécuter une transaction (achat ou vente).
    public boolean executeTransaction(String username,
                                      String assetName,
                                      double cashValue,
                                      boolean isBuying,
                                      AssetType assetType,
                                      User user) {
        try {
            // Obtenir le prix actuel de l'actif.
            double assetPrice = getCurrentPrice(assetName, assetType);
            // Calculer la quantité d'actif en fonction du montant de l'argent disponible.
            double assetAmount = cashValue / assetPrice;

            // Lire les actifs actuels de l'utilisateur à partir du fichier Excel.
            Map<String, Double> userAssets = excelReader.readUserAssets(username, "./Files/users.xlsx");
            // Récupérer les balances actuelles.
            double balanceTotal = userAssets.getOrDefault("balance_total", 0.0);
            double balanceCrypto = userAssets.getOrDefault("balance_crypto", 0.0);
            double balanceStocks = userAssets.getOrDefault("balance_stocks", 0.0);
            double balanceCash = userAssets.getOrDefault("balance_cash", 0.0);

            if (isBuying) {
                // Gérer l'achat d'actifs.
                if (balanceCash < cashValue) {
                    // Vérifier si l'utilisateur a suffisamment de fonds.
                    System.out.println("Insufficient funds for purchase.");
                    return false;
                }
                // Mettre à jour les soldes après l'achat.
                balanceCash -= cashValue;
                assetAmount += userAssets.getOrDefault(assetName, 0.0);
                if (assetType == AssetType.CRYPTO) {
                    balanceCrypto += cashValue;
                } else {
                    balanceStocks += cashValue;
                }
            } else {
                // Gérer la vente d'actifs.
                double currentAssetAmount = userAssets.getOrDefault(assetName, 0.0);
                double assetValueToSell = assetAmount * assetPrice;

                if (currentAssetAmount < assetAmount) {
                    // Vérifier si l'utilisateur a suffisamment d'actifs à vendre.
                    System.out.println("Insufficient asset quantity for sale.");
                    return false;
                }
                // Mettre à jour les soldes après la vente.
                balanceCash += assetValueToSell;
                assetAmount = currentAssetAmount - assetAmount;
                if (assetType == AssetType.CRYPTO) {
                    balanceCrypto -= assetValueToSell;
                } else {
                    balanceStocks -= assetValueToSell;
                }
            }

            // Calculer la balance totale.
            balanceTotal = balanceCash + balanceCrypto + balanceStocks;

            // Mettre à jour les actifs de l'utilisateur dans le fichier Excel.
            userAssets.put("balance_total", balanceTotal);
            userAssets.put("balance_crypto", balanceCrypto);
            userAssets.put("balance_stocks", balanceStocks);
            userAssets.put("balance_cash", balanceCash);
            userAssets.put(assetName, assetAmount);

            excelWriter.updateUserAssets(username, userAssets, "./Files/users.xlsx");

            // Mettre à jour les informations de l'utilisateur dans l'application.
            user.setBalanceStock(balanceStocks);
            user.setBalanceCrypto(balanceCrypto);
            user.setBalanceCash(balanceCash);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Méthode pour obtenir le prix actuel d'un actif.
    private double getCurrentPrice(String assetName, AssetType assetType) {
        try {
            double price;
            if (assetType == AssetType.CRYPTO) {
                // Obtenir le prix pour les cryptomonnaies.
                price = coinGeckoService.getCurrentCryptoPrice(assetName);
            } else {
                // Obtenir le prix pour les actions.
                price = alphaVantageService.getCurrentStockPrice(assetName);
            }
            // Utiliser le prix de l'API ou, si indisponible, celui du fichier Excel.
            return price != -1 ? price : excelReader.readPrice(assetName, assetType, "./Files/ActionCrypto.xlsx");
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
