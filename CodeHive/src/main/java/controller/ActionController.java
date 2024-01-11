package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import model.Action;
import model.AlphaVantageService;
import org.json.JSONObject;

public class ActionController {

    @FXML
    private TableView<Action> actionTable;

    @FXML
    private TableColumn<Action, String> symbolColumn;

    @FXML
    private TableColumn<Action, Double> priceColumn;

    @FXML
    private TableColumn<Action, Double> volumeColumn;

    private final AlphaVantageService alphaVantageService = new AlphaVantageService();

    @FXML
    public void initialize() {
        symbolColumn.setCellValueFactory(new PropertyValueFactory<>("symbol"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        volumeColumn.setCellValueFactory(new PropertyValueFactory<>("volume"));

        loadActionData();
    }

    private void loadActionData() {
        ObservableList<Action> actions = FXCollections.observableArrayList();

        String[] symbols = {"AAPL", "MSFT", "GOOGL", "TSLA", "AMZN", "NVDA", "NFLX", "INTC", "AMD"};

        for (String symbol : symbols) {
            String rawData = alphaVantageService.getStockData(symbol);

            if (rawData != null) {
                JSONObject jsonObject = new JSONObject(rawData);

                if (jsonObject.has("Time Series (Daily)")) {
                    JSONObject timeSeries = jsonObject.getJSONObject("Time Series (Daily)");

                    // Itérez sur les données ici
                    for (String date : timeSeries.keySet()) {
                        JSONObject dailyData = timeSeries.getJSONObject(date);
                        Action action = new Action(
                                symbol, // Utilisez le symbole actuel de la boucle
                                dailyData.getDouble("4. close"),
                                dailyData.getDouble("5. volume")
                        );
                        actions.add(action);
                        break; // Actuellement, nous ajoutons uniquement le dernier point de données
                    }
                } else {
                    System.out.println("Clé 'Time Series (Daily)' non trouvée dans les données JSON pour le symbole " + symbol);
                }
            } else {
                System.out.println("Les données brutes (rawData) sont nulles pour le symbole " + symbol);
            }
        }

        actionTable.setItems(actions);
    }


    @FXML
    protected void handleRefreshButtonAction(ActionEvent event) {
        loadActionData();
    }
}
