package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import model.Crypto;
import model.CoinGeckoService;
import org.json.JSONArray;
import org.json.JSONObject;

public class CryptoController {

    @FXML
    private TableView<Crypto> cryptoTable;

    @FXML
    private TableColumn<Crypto, String> nameColumn;

    @FXML
    private TableColumn<Crypto, Double> priceColumn;

    @FXML
    private TableColumn<Crypto, Double> marketCapColumn;

    private final CoinGeckoService coinGeckoService = new CoinGeckoService();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        marketCapColumn.setCellValueFactory(new PropertyValueFactory<>("marketCap"));

        // Load initial data
        loadCryptoData();
    }

    private void loadCryptoData() {
        String rawData = coinGeckoService.getCryptoData();
        ObservableList<Crypto> cryptos = FXCollections.observableArrayList();

        if (rawData != null) {
            JSONArray jsonArray = new JSONArray(rawData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Crypto crypto = new Crypto(
                        jsonObject.getString("name"),
                        jsonObject.getDouble("current_price"),
                        jsonObject.getDouble("market_cap")
                );
                cryptos.add(crypto);
            }
        }

        cryptoTable.setItems(cryptos);
    }

    @FXML
    protected void handleRefreshButtonAction(ActionEvent event) {
        loadCryptoData();
    }
}
