package fr.istic.vv;



import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

public class HistogramExample extends Application {

    @Override
    public void start(Stage stage) {
        // Données pour l'exemple
        List<Integer> values = Arrays.asList(1, 1, 2, 1);

        // Création du graphique à barres
        BarChart<String, Number> barChart = createBarChart(values);

        // Création de la scène
        Scene scene = new Scene(barChart, 600, 400);

        // Configuration de la scène
        stage.setTitle("Histogram of cyclomatique Numbers");
        stage.setScene(scene);

        // Affichage de la scène
        stage.show();
    }

    private BarChart<String, Number> createBarChart(List<Integer> values) {
        // Création des axes
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        // Configuration des axes
        xAxis.setLabel("Methode Number");
        yAxis.setLabel("cyclomatique Number");

        // Création de la série de données
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Histogram");

        // Ajout des données à la série
        for (int i = 0; i < values.size(); i++) {
            series.getData().add(new XYChart.Data<>(String.valueOf(i + 1), values.get(i)));
        }

        // Ajout de la série au graphique
        barChart.getData().add(series);

        return barChart;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
