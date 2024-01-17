package model;

import java.io.IOException;
import java.util.Map;

public class StartupManager {

    private CoinGeckoService coinGeckoService = new CoinGeckoService();
    private AlphaVantageService alphaVantageService = new AlphaVantageService();
    private ExcelWriter excelWriter = new ExcelWriter();

    public void initializeApplicationData() {
        // Tentez de récupérer les prix des cryptomonnaies
        Map<String, Double> cryptoPrices = null;
        try {
            cryptoPrices = coinGeckoService.getAllCryptoPrices();
        } catch (Exception e) {
            System.err.println("Failed to fetch crypto prices: " + e.getMessage());
        }

        // Tentez de récupérer les prix des actions
        Map<String, Double> stockPrices = null;
        try {
            stockPrices = alphaVantageService.getAllStockPrices();
        } catch (Exception e) {
            System.err.println("Failed to fetch stock prices: " + e.getMessage());
        }

        // Si les deux requêtes sont réussies, mettez à jour l'Excel
        if (cryptoPrices != null && !cryptoPrices.isEmpty() && stockPrices != null && !stockPrices.isEmpty()) {
            try {
                excelWriter.writeDataToExcel(cryptoPrices, stockPrices, "./Files/ActionCrypto.xlsx");
            } catch (IOException e) {
                System.err.println("Failed to write data to Excel: " + e.getMessage());
            }
        } else {
            System.out.println("API request failed or limit reached. Using old data.");
        }
    }

}
