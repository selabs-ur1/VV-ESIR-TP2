package fr.istic.vv;

import com.github.javaparser.Problem;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.SourceRoot;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Should provide the path to the source code");
            System.exit(1);
        }

        File file = new File(args[0]);
        if (!file.exists() || !file.isDirectory() || !file.canRead()) {
            System.err.println("Provide a path to an existing readable directory");
            System.exit(2);
        }

        SourceRoot root = new SourceRoot(file.toPath());
        PublicElementsPrinter printer = new PublicElementsPrinter();

        root.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> {
                unit.accept(printer, null);
                saveReportToFile(printer.getReport(), "CyclomaticComplexityReport.txt");
                generateHistogram(printer.getReport(), "CyclomaticComplexityHistogram.txt");
            });
            return SourceRoot.Callback.Result.DONT_SAVE;
        });
    }

     private static void saveReportToFile(List<String> report, String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            // Write header
            writer.println("Package\tClass\tMethod\t\t\t\tParameters\tCyclomatic Complexity");

            // Write data
            for (String entry : report) {
                writer.println(entry);
            }

            System.out.println("Report saved to: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}


