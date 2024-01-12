package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Version;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class CoinGeckoService {

    private static final String COINGECKO_API_URL = "https://api.coingecko.com/api/v3/";
    private static final String API_KEY = "CG-iX3qPUyNu3gXTELkh24958rh"; // Votre clé API
    private long lastRequestTime = 0;
    private static final long REQUEST_INTERVAL = 2000; // 2 secondes entre les requêtes

    public String getCryptoData() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastRequestTime < REQUEST_INTERVAL) {
            try {
                Thread.sleep(REQUEST_INTERVAL - (currentTime - lastRequestTime));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        HttpClient client = HttpClient.newBuilder()
                .version(Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(COINGECKO_API_URL + "coins/markets?vs_currency=usd&order=market_cap_desc&per_page=10&page=1&api_key=" + API_KEY)) // Ajout de la clé API
                .timeout(Duration.ofMinutes(2))
                .build();

        lastRequestTime = System.currentTimeMillis();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, Double> getAllCryptoPrices() {
        Map<String, Double> prices = new HashMap<>();
        String rawData = getCryptoData(); // Suppose que cette méthode renvoie les données brutes de l'API
        if (rawData != null) {
            JSONArray jsonArray = new JSONArray(rawData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("id"); // ou "name" selon ce que vous voulez utiliser comme clé
                double price = jsonObject.getDouble("current_price");
                prices.put(name, price);
            }
        }
        return prices;
    }

    public double getCurrentCryptoPrice(String cryptoId) {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .connectTimeout(Duration.ofSeconds(20))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(COINGECKO_API_URL + "simple/price?ids=" + cryptoId + "&vs_currencies=usd"))
                    .timeout(Duration.ofMinutes(1))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonResponse = new JSONObject(response.body());
            return jsonResponse.getJSONObject(cryptoId).getDouble("usd");
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

}

