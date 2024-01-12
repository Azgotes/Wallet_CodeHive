package model;

import java.io.IOException;
import java.util.Map;

public class StartupManager {

    private CoinGeckoService coinGeckoService = new CoinGeckoService();
    private AlphaVantageService alphaVantageService = new AlphaVantageService();
    private ExcelWriter excelWriter = new ExcelWriter();

    public void initializeApplicationData() {
        Map<String, Double> cryptoPrices = coinGeckoService.getAllCryptoPrices();
        Map<String, Double> stockPrices = alphaVantageService.getAllStockPrices();

        try {
            excelWriter.writeDataToExcel(cryptoPrices, stockPrices, "./Files/ActionCrypto.xlsx");
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception properly
        }
    }
}

