package controller;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableView;

public class WalletController {
    @FXML
    private LineChart<String, Number> balanceChart;
    @FXML
    private PieChart assetDistributionChart;
    @FXML
    private TableView<?> cryptoTable;

    @FXML
    public void initialize() {
        // Initialiser le graphique de solde
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        // Ajouter des données à la série (faux exemple)
        series.getData().add(new XYChart.Data<>("12:57 PM", 32.84));
        balanceChart.getData().add(series);

        // Initialiser le diagramme de répartition des actifs
        PieChart.Data slice1 = new PieChart.Data("Crypto", 86.2);
        PieChart.Data slice2 = new PieChart.Data("Action", 13.8);
        assetDistributionChart.getData().addAll(slice1, slice2);

        // Initialiser le tableau des cryptomonnaies
        // tableau avec des données réelles seront placé ici
    }
}

