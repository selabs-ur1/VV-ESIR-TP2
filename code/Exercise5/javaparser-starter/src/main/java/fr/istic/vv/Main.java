package fr.istic.vv;

import com.github.javaparser.Problem;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.SourceRoot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    CyclomaticComplexity printer = new CyclomaticComplexity();
    StringBuilder htmlReport = new StringBuilder();
    htmlReport.append("<html><head><title>Field Report</title>");
    htmlReport.append("<style>" +
        "table, td {border: 1px solid #333; margin: 0;}" +
        "thead, tfoot {background-color: #333; color: #fff;" +
        "</style>");
    htmlReport.append("</head><body>");

    htmlReport.append("<h1>Table:</h1><table>\n" + //
        "  <thead>\n" + //
        "    <tr>\n" + //
        "      <th>Package</th><th>Class</th><th>Method</th><th>Cyclomatic Complexity</th>\n" + //
        "    </tr>\n" + //
        "  </thead>" +
        "  <tbody>");

    root.parse("", (localPath, absolutePath, result) -> {
      result.ifSuccessful(unit -> {
        unit.accept(printer, null);
        htmlReport.append(generateHtmlReport(unit, printer));
      });
      return SourceRoot.Callback.Result.DONT_SAVE;
    });
    htmlReport.append("</tbody></table>");
    int maxCC = 0;
    int totalMethods = printer.getMethodsInfo().size();
    for (Integer cc : printer.getDistribution().keySet()) {
      if (cc > maxCC) {
        maxCC = cc;
      }
    }
    htmlReport.append("<h1>Histogramme:</h1><p>");
    for (int i = 0; i < maxCC; i++) {
      String s = "";
      for (int j = 0; j < (printer.getDistribution().getOrDefault(i + 1, 0) / (float) totalMethods) * 100; j++) {
        s += "-";
      }
      htmlReport
          .append((i + 1) + ": " + s + "<br>");
    }

    htmlReport.append("</p>");
    htmlReport.append("</body></html>");

    String outputPath = "report.html";
    writeHtmlReport(htmlReport.toString(), outputPath);
  }

  private static String generateHtmlReport(CompilationUnit unit, CyclomaticComplexity cyclomaticComplexity) {
    StringBuilder html = new StringBuilder();

    cyclomaticComplexity.getMethodsInfo().forEach(methodInfo -> {
      String methodDeclaration = methodInfo.getMethodName() + "(";
      List<String> types = methodInfo.getParametersTypes();
      for (int i = 0; i < types.size(); i++) {
        methodDeclaration += types.get(i);
        if (i < types.size() - 1) {
          methodDeclaration += ", ";
        }
      }
      methodDeclaration += ")";
      html.append("<tr>");
      html.append("<td>").append(methodInfo.getPackageName()).append("</td>");
      html.append("<td>").append(methodInfo.getClassName()).append("</td>");
      html.append("<td>").append(methodDeclaration).append("</td>");
      html.append("<td>").append(methodInfo.getCyclomaticComplexity()).append("</td>");
      html.append("</tr>");
    });
    return html.toString();
  }

  private static void writeHtmlReport(String htmlContent, String outputPath) {
    try (FileWriter writer = new FileWriter(outputPath)) {
      writer.write(htmlContent);
      System.out.println("HTML report written to: " + outputPath);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
