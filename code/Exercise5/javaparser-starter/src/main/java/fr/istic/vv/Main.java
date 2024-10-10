package fr.istic.vv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import com.github.javaparser.utils.SourceRoot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.statistics.HistogramDataset;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Veuillez fournir le chemin du code source");
            System.exit(1);
        }

        File file = new File(args[0]);
        if (!file.exists() || !file.isDirectory() || !file.canRead()) {
            System.err.println("Veuillez fournir un chemin vers un répertoire existant et lisible");
            System.exit(2);
        }

        SourceRoot root = new SourceRoot(file.toPath());
        CyclomaticComplexityVisitor visitor = new CyclomaticComplexityVisitor();

        root.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> unit.accept(visitor, null));
            return SourceRoot.Callback.Result.DONT_SAVE;
        });

        generateReport(visitor.getMethodComplexityMap());
        generateHistogram(visitor.getMethodComplexityMap());
    }

    private static void generateReport(Map<String, CyclomaticComplexityVisitor.MethodInfo> methodComplexityMap) throws IOException {
        try (FileWriter writer = new FileWriter("cyclomatic_complexity_report.csv")) {
            writer.write("Class.Method,Parameter Types,CC\n");
            for (CyclomaticComplexityVisitor.MethodInfo info : methodComplexityMap.values()) {
                writer.write(info.className + "." + info.methodName + "," + info.parameterTypes + "," + info.cyclomaticComplexity + "\n");
            }
        }
    }

    private static void generateHistogram(Map<String, CyclomaticComplexityVisitor.MethodInfo> methodComplexityMap) throws IOException {
        HistogramDataset dataset = new HistogramDataset();
        double[] ccValues = methodComplexityMap.values().stream()
            .mapToDouble(info -> info.cyclomaticComplexity)
            .toArray();

        dataset.addSeries("Complexité Cyclomatique", ccValues, 5); // Ajustez la largeur des bins si nécessaire

        JFreeChart histogram = ChartFactory.createHistogram(
            "Histogramme de la Complexité Cyclomatique",
            "Valeur CC",
            "Fréquence",
            dataset
        );

        ChartUtils.saveChartAsPNG(new File("cc_histogram.png"), histogram, 800, 600);
    }
}
