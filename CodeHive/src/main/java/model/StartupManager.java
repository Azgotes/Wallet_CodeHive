package model;

import java.io.IOException;
import java.util.Map;

public class StartupManager {

    // Instances des services pour récupérer les données.
    private CoinGeckoService coinGeckoService = new CoinGeckoService();
    private AlphaVantageService alphaVantageService = new AlphaVantageService();
    private ExcelWriter excelWriter = new ExcelWriter();

    // Méthode pour initialiser les données de l'application.
    public void initializeApplicationData() {
        // Initialisation des variables pour stocker les prix des cryptomonnaies et des actions.
        Map<String, Double> cryptoPrices = null;
        Map<String, Double> stockPrices = null;

        // Tentez de récupérer les prix des cryptomonnaies.
        try {
            cryptoPrices = coinGeckoService.getAllCryptoPrices();
        } catch (Exception e) {
            // En cas d'échec, affichez un message d'erreur.
            System.err.println("Failed to fetch crypto prices: " + e.getMessage());
        }

        // Tentez de récupérer les prix des actions.
        try {
            stockPrices = alphaVantageService.getAllStockPrices();
        } catch (Exception e) {
            // En cas d'échec, affichez un message d'erreur.
            System.err.println("Failed to fetch stock prices: " + e.getMessage());
        }

        // Vérifiez si les deux requêtes sont réussies.
        if (cryptoPrices != null && !cryptoPrices.isEmpty() && stockPrices != null && !stockPrices.isEmpty()) {
            // Si les données sont disponibles, écrivez-les dans le fichier Excel.
            try {
                excelWriter.writeDataToExcel(cryptoPrices, stockPrices, "./Files/ActionCrypto.xlsx");
            } catch (IOException e) {
                // En cas d'erreur d'écriture, affichez un message d'erreur.
                System.err.println("Failed to write data to Excel: " + e.getMessage());
            }
        } else {
            // Si l'une des requêtes échoue, utilisez les anciennes données.
            System.out.println("API request failed or limit reached. Using old data.");
        }
    }

}
