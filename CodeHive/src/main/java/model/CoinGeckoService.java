package model;

// Importation des bibliothèques nécessaires pour le traitement JSON, les requêtes HTTP, et la gestion du temps.
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class CoinGeckoService {

    // URL de base de l'API CoinGecko et clé API.
    private static final String COINGECKO_API_URL = "https://api.coingecko.com/api/v3/";
    private static final String API_KEY = "CG-iX3qPUyNu3gXTELkh24958rh"; //clé API

    // Gestion des intervalles entre les requêtes pour éviter le flood.
    private long lastRequestTime = 0;
    private static final long REQUEST_INTERVAL = 2000; // 2 secondes entre les requêtes

    // Méthode pour obtenir les données de cryptomonnaies.
    public String getCryptoData() {
        // Gestion de l'intervalle entre les requêtes.
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastRequestTime < REQUEST_INTERVAL) {
            try {
                Thread.sleep(REQUEST_INTERVAL - (currentTime - lastRequestTime));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Configuration du client HTTP pour la requête.
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        // Construction de la requête HTTP.
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(COINGECKO_API_URL + "coins/markets?vs_currency=usd&order=market_cap_desc&per_page=10&page=1&api_key=" + API_KEY))
                .timeout(Duration.ofMinutes(2))
                .build();

        // Mise à jour du temps de la dernière requête.
        lastRequestTime = System.currentTimeMillis();

        // Envoi de la requête et gestion des réponses ou des erreurs.
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Méthode pour obtenir les prix de toutes les cryptomonnaies.
    public Map<String, Double> getAllCryptoPrices() {
        Map<String, Double> prices = new HashMap<>();
        String rawData = getCryptoData(); // Récupère les données brutes de l'API
        if (rawData != null) {
            JSONArray jsonArray = new JSONArray(rawData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("id"); // Nom de la cryptomonnaie
                double price = jsonObject.getDouble("current_price"); // Prix actuel
                prices.put(name, price);
            }
        }
        return prices;
    }

    // Méthode pour obtenir le prix actuel d'une cryptomonnaie spécifique.
    public double getCurrentCryptoPrice(String cryptoId) {
        try {
            // Configuration et envoi de la requête HTTP.
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
