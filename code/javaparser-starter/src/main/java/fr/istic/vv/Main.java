package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.SourceRoot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

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

        // First, analyze for fields with no public getters
        NoGetterFieldVisitor noGetterVisitor = new NoGetterFieldVisitor();
        root.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> unit.accept(noGetterVisitor, null));
            return SourceRoot.Callback.Result.DONT_SAVE;
        });

        // Generate and print the report for fields with no public getters
        String noGetterReport = noGetterVisitor.generateReport();
        System.out.println("No Getter Fields Report:\n" + noGetterReport);

        // Second, analyze for Cyclomatic Complexity
        CyclomaticComplexityVisitor ccVisitor = new CyclomaticComplexityVisitor();
        root.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> unit.accept(ccVisitor, null));
            return SourceRoot.Callback.Result.DONT_SAVE;
        });

        // Generate and print the report for Cyclomatic Complexity
        String ccReport = ccVisitor.generateReport();
        System.out.println("Cyclomatic Complexity Report:\n" + ccReport);

        // Optionally, you can save the reports to files if needed.
        // noGetterVisitor.saveReportToFile("no_getter_report.txt");
        // ccVisitor.saveReportToFile("cc_report.txt");
    }
}

class NoGetterFieldVisitor extends VoidVisitorAdapter<Void> {
    private StringBuilder reportBuilder = new StringBuilder();

    @Override
    public void visit(ClassOrInterfaceDeclaration classDeclaration, Void arg) {
        // Visit fields in the class
        for (FieldDeclaration fieldDeclaration : classDeclaration.getFields()) {
            if (fieldDeclaration.isPrivate() && !hasPublicGetter(classDeclaration, fieldDeclaration)) {
                // Append the information to the report
                reportBuilder.append("Field: ").append(fieldDeclaration.getVariables().get(0)).append("\n");
                reportBuilder.append("Class: ").append(classDeclaration.getNameAsString()).append("\n");
                reportBuilder.append("Package: ").append(classDeclaration.findCompilationUnit().get().getPackageDeclaration().map(p -> p.getNameAsString()).orElse("N/A")).append("\n\n");
            }
        }
        super.visit(classDeclaration, arg);
    }

    // Check if the field has a public getter in the same class
    private boolean hasPublicGetter(ClassOrInterfaceDeclaration classDeclaration, FieldDeclaration fieldDeclaration) {
        String fieldName = fieldDeclaration.getVariable(0).getNameAsString();
        String getterMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

        // Check for a method with the generated getter name
        return classDeclaration.getMethodsByName(getterMethodName).stream()
                .anyMatch(methodDeclaration -> methodDeclaration.isPublic() && methodDeclaration.getParameters().isEmpty());
    }

    // Generate the final report
    public String generateReport() {
        return reportBuilder.toString();
    }
}

class CyclomaticComplexityVisitor extends VoidVisitorAdapter<Void> {
    private StringBuilder reportBuilder = new StringBuilder();

    @Override
    public void visit(ClassOrInterfaceDeclaration classDeclaration, Void arg) {
        // Visit methods in the class
        for (com.github.javaparser.ast.body.MethodDeclaration methodDeclaration : classDeclaration.getMethods()) {
            int cc = computeCyclomaticComplexity(methodDeclaration);

            // Append the information to the report
            reportBuilder.append("Package: ").append(classDeclaration.findCompilationUnit().get().getPackageDeclaration().map(p -> p.getNameAsString()).orElse("N/A")).append("\n");
            reportBuilder.append("Class: ").append(classDeclaration.getNameAsString()).append("\n");
            reportBuilder.append("Method: ").append(methodDeclaration.getNameAsString()).append("\n");
            reportBuilder.append("Parameters: ").append(getParameterTypes(methodDeclaration)).append("\n");
            reportBuilder.append("CC: ").append(cc).append("\n\n");
        }

        super.visit(classDeclaration, arg);
    }

    private int computeCyclomaticComplexity(com.github.javaparser.ast.body.MethodDeclaration method) {
        // Your Cyclomatic Complexity calculation logic goes here
        // For simplicity, let's assume each decision point (if, while, for, etc.) contributes 1 to CC
        return method.findAll(com.github.javaparser.ast.stmt.IfStmt.class).size() +
                method.findAll(com.github.javaparser.ast.stmt.WhileStmt.class).size() +
                method.findAll(com.github.javaparser.ast.stmt.ForStmt.class).size() +
                method.findAll(com.github.javaparser.ast.stmt.SwitchStmt.class).size() + 1;
    }

    private String getParameterTypes(com.github.javaparser.ast.body.MethodDeclaration method) {
        // Your logic to obtain parameter types goes here
        // For simplicity, let's assume there are no parameters
        return "N/A";
    }

    // Generate the final report
    public String generateReport() {
        return reportBuilder.toString();
    }

    // Optionally, you can add a method to save the report to a file
    public void saveReportToFile(String filePath) {
        // Your logic to save the report to a file goes here
    }
}