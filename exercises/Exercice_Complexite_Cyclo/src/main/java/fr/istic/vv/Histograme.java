package fr.istic.vv;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.*;

public class Histograme {

    HistogramDataset dataset;
    double[] ccValues;
    double max;

    public Histograme(List<Double> ccValues) {
        dataset = new HistogramDataset();
        this.ccValues = ccValues.stream().mapToDouble(d -> d).toArray();
        max = Collections.max(ccValues);
    }

    private void createDataset(int bins) {
        // Créer le dataset
        dataset.setType(HistogramType.RELATIVE_FREQUENCY);
        dataset.addSeries("CC", ccValues, bins); // Le '100' est le nombre de bins
    }

    private JFreeChart createGraphique(int bins) {
        createDataset(bins);

        JFreeChart histogram = ChartFactory.createHistogram(
                "Histogramme de Complexité Cyclomatique\nEffectif total : " + ccValues.length,
                "Valeur CC",
                "Fréquence",
                dataset);

        return histogram;
    }

    public void afficherGraph() {
        int bins = (int) max / 2;
        JFreeChart histogram = createGraphique(bins);

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Histogramme CC");
            frame.setContentPane(new ChartPanel(histogram));
            frame.pack();
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });
    }
}
