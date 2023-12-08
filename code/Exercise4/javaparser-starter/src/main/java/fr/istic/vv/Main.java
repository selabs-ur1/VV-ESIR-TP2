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
    NoGetter printer = new NoGetter();
    StringBuilder htmlReport = new StringBuilder();

    root.parse("", (localPath, absolutePath, result) -> {
      result.ifSuccessful(unit -> {
        unit.accept(printer, null);
        htmlReport.append(generateHtmlReport(unit, printer));
      });
      return SourceRoot.Callback.Result.DONT_SAVE;
    });

    String outputPath = "report.html";
    writeHtmlReport(htmlReport.toString(), outputPath);
  }

  private static String generateHtmlReport(CompilationUnit unit, NoGetter noGetter) {
    StringBuilder html = new StringBuilder();
    html.append("<html><head><title>Field Report</title></head><body>");

    html.append("<p><i>Field Report for File: ").append(unit.getStorage().get().getPath()).append("</i></p>");

    noGetter.getFieldsWithoutGetters().forEach(fieldInfo -> {
      html.append("<p>");
      html.append("⚠️ Package <b>")
          .append(fieldInfo.getPackageName()).append("</b>: The class <b>")
          .append(fieldInfo.getClassName())
          .append("</b> has no getter for the field <b>")
          .append(fieldInfo.getFieldName()).append("</b><br>");
      html.append("</p>");
    });

    html.append("</body></html>");
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
