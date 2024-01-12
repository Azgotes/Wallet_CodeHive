package model;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class AlphaVantageService {

    private static final String ALPHA_VANTAGE_API_URL = "https://www.alphavantage.co/query";
    private static final String ALPHA_VANTAGE_API_KEY = "60I08T762JWC7GPK";

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
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

