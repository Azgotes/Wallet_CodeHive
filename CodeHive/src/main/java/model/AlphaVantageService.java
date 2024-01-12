package model;

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

    private static final String ALPHA_VANTAGE_API_URL = "https://www.alphavantage.co/query";
    private static final String ALPHA_VANTAGE_API_KEY = "GHKDS5IBYS6SYBLM";

    public String getStockData(String symbol) {
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        String endpoint = String.format("%s?function=TIME_SERIES_DAILY&symbol=%s&apikey=%s", ALPHA_VANTAGE_API_URL, symbol, ALPHA_VANTAGE_API_KEY);

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(endpoint))
                .timeout(Duration.ofMinutes(2))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            System.out.println("Response for " + symbol + ": " + responseBody); // Ajoutez cette ligne
            return responseBody;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public Map<String, Double> getAllStockPrices() {
        Map<String, Double> prices = new HashMap<>();
        String[] symbols = {"AAPL", "MSFT", "GOOGL", "TSLA", "AMZN", "NVDA", "NFLX", "INTC", "AMD"};

        for (String symbol : symbols) {
            String rawData = getStockData(symbol);
            if (rawData != null) {
                try {
                    JSONObject jsonObject = new JSONObject(rawData);
                    // Vérifiez si la clé "Time Series (Daily)" existe
                    if (!jsonObject.has("Time Series (Daily)")) {
                        System.err.println("Time Series (Daily) not found for symbol: " + symbol);
                        continue; // Passez au symbole suivant si la clé n'existe pas
                    }
                    JSONObject timeSeries = jsonObject.getJSONObject("Time Series (Daily)");
                    String latestDate = timeSeries.keys().next(); // Obtenez la date la plus récente disponible
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
}


