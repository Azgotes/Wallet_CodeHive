package model;

// Importation des bibliothèques nécessaires.
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class AlphaVantageService {

    // Constantes pour l'URL de l'API et la clé API.
    private static final String ALPHA_VANTAGE_API_URL = "https://www.alphavantage.co/query";
    private static final String ALPHA_VANTAGE_API_KEY = "GHKDS5IBYS6SYBLM";

    // Méthode pour récupérer les données boursières d'une action spécifique.
    public String getStockData(String symbol) {
        // Configuration du client HTTP.
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        // Construction de l'URL de requête.
        String endpoint = String.format("%s?function=TIME_SERIES_DAILY&symbol=%s&apikey=%s", ALPHA_VANTAGE_API_URL, symbol, ALPHA_VANTAGE_API_KEY);

        // Création de la requête HTTP.
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(endpoint))
                .timeout(Duration.ofMinutes(2))
                .build();

        // Envoi de la requête et récupération de la réponse.
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            System.out.println("Response for " + symbol + ": " + responseBody);
            return responseBody;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Méthode pour récupérer les prix de plusieurs actions.
    public Map<String, Double> getAllStockPrices() {
        Map<String, Double> prices = new HashMap<>();
        String[] symbols = {"AAPL", "MSFT", "GOOGL", "TSLA", "AMZN", "NVDA", "NFLX", "INTC", "AMD"};

        for (String symbol : symbols) {
            String rawData = getStockData(symbol);
            if (rawData != null) {
                try {
                    JSONObject jsonObject = new JSONObject(rawData);
                    if (!jsonObject.has("Time Series (Daily)")) {
                        System.err.println("Time Series (Daily) not found for symbol: " + symbol);
                        continue;
                    }
                    JSONObject timeSeries = jsonObject.getJSONObject("Time Series (Daily)");
                    String latestDate = timeSeries.keys().next();
                    JSONObject latestData = timeSeries.getJSONObject(latestDate);
                    double price = latestData.getDouble("4. close");
                    prices.put(symbol, price);
                } catch (JSONException e) {
                    System.err.println("Invalid JSON format for symbol: " + symbol);
                    e.printStackTrace();
                }
            }
        }
        return prices;
    }

    // Méthode pour obtenir le prix actuel d'une action spécifique.
    public double getCurrentStockPrice(String symbol) {
        try {
            String rawData = getStockData(symbol);
            if (rawData != null) {
                JSONObject jsonObject = new JSONObject(rawData);
                if (!jsonObject.has("Time Series (Daily)")) {
                    return -1;
                }
                JSONObject timeSeries = jsonObject.getJSONObject("Time Series (Daily)");
                String latestDate = timeSeries.keys().next();
                JSONObject latestData = timeSeries.getJSONObject(latestDate);
                return latestData.getDouble("4. close");
            }
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
