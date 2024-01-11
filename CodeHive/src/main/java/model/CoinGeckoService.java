package model;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Version;
import java.time.Duration;

public class CoinGeckoService {

    private static final String COINGECKO_API_URL = "https://api.coingecko.com/api/v3/";

    public String getCryptoData() {
        HttpClient client = HttpClient.newBuilder()
                .version(Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(COINGECKO_API_URL + "coins/markets?vs_currency=usd&order=market_cap_desc&per_page=10&page=1"))
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
