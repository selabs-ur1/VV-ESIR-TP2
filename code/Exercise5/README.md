# Code of your exercise

Put here all the code created for this exercise

## `Complexity.java`

```java
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Complexity {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Utilisation : java Complexity <chemin_vers_le_dossier_du_code>");
            System.exit(1);
        }

        String chemin_dossier = args[0];
        analyse(chemin_dossier);
    }

    private static void analyse(String sourcePath) {
        Map<MethodInfo, Integer> listeDesMethodes = new HashMap<>();

        try {
            File sourceFolder = new File(sourcePath);
            if (sourceFolder.isDirectory()) {
                for (File file : Objects.requireNonNull(sourceFolder.listFiles())) {
                    if (file.isFile() && file.getName().endsWith(".java")) {
                        CompilationUnit cu = StaticJavaParser.parse(file);

                        cu.accept(new ComplexityVisitor(), listeDesMethodes);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        genererRapport(listeDesMethodes, "rapport.csv");
    }

    private static void genererRapport(Map<MethodInfo, Integer> methodInfoList, String outputFile) {
        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write("Méthode,Classe,Package,CC\n");
            for (Map.Entry<MethodInfo, Integer> methodInfo : methodInfoList.entrySet()) {
                writer.write(methodInfo.getKey().name + "," + methodInfo.getKey().declaringClass + "," + methodInfo.getKey().packageName + "," + (methodInfo.getValue() + 1) + "\n");
            }
            System.out.println("Le rapport se nomme : " + outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ComplexityVisitor extends VoidVisitorAdapter<Map<MethodInfo, Integer>> {
        protected String nomMethode;

        public void visit(MethodDeclaration declaration, Map<MethodInfo, Integer> methodInfoList) {
            nomMethode = declaration.getNameAsString();

            super.visit(declaration, methodInfoList);
        }

        @Override
        public void visit(ForEachStmt statement, Map<MethodInfo, Integer> methodInfoList) {
            super.visit(statement, methodInfoList);

            CompilationUnit current_cu = statement.findCompilationUnit().get();

            String declaringClass = current_cu.getPrimaryTypeName().get();
            String packageName = current_cu.getPackageDeclaration().map(pd -> pd.getName().asString()).orElse("");

            MethodInfo methodInfo = new MethodInfo(nomMethode, declaringClass, packageName);

            Integer curCC = methodInfoList.getOrDefault(methodInfo, 0) + 1;
            methodInfoList.put(methodInfo, curCC);
        }

        @Override
        public void visit(ForStmt statement, Map<MethodInfo, Integer> methodInfoList) {
            super.visit(statement, methodInfoList);

            CompilationUnit current_cu = statement.findCompilationUnit().get();

            String declaringClass = current_cu.getPrimaryTypeName().get();
            String packageName = current_cu.getPackageDeclaration().map(pd -> pd.getName().asString()).orElse("");

            MethodInfo methodInfo = new MethodInfo(nomMethode, declaringClass, packageName);

            Integer curCC = methodInfoList.getOrDefault(methodInfo, 0) + 1;
            methodInfoList.put(methodInfo, curCC);
        }

        @Override
        public void visit(IfStmt statement, Map<MethodInfo, Integer> methodInfoList) {
            super.visit(statement, methodInfoList);

            CompilationUnit current_cu = statement.findCompilationUnit().get();

            String declaringClass = current_cu.getPrimaryTypeName().get();
            String packageName = current_cu.getPackageDeclaration().map(pd -> pd.getName().asString()).orElse("");

            MethodInfo methodInfo = new MethodInfo(nomMethode, declaringClass, packageName);

            Integer curCC = methodInfoList.getOrDefault(methodInfo, 0) + 1;
            methodInfoList.put(methodInfo, curCC);
        }

        @Override
        public void visit(SwitchEntry statement, Map<MethodInfo, Integer> methodInfoList) {
            super.visit(statement, methodInfoList);

            for (Statement st : statement.getStatements()) {
                CompilationUnit current_cu = st.findCompilationUnit().get();

                String declaringClass = current_cu.getPrimaryTypeName().get();
                String packageName = current_cu.getPackageDeclaration().map(pd -> pd.getName().asString()).orElse("");

                MethodInfo methodInfo = new MethodInfo(nomMethode, declaringClass, packageName);

                Integer curCC = methodInfoList.getOrDefault(methodInfo, 0) + 1;
                methodInfoList.put(methodInfo, curCC);
            }

        }

        @Override
        public void visit(ThrowStmt statement, Map<MethodInfo, Integer> methodInfoList) {
            super.visit(statement, methodInfoList);

            CompilationUnit current_cu = statement.findCompilationUnit().get();

            String declaringClass = current_cu.getPrimaryTypeName().get();
            String packageName = current_cu.getPackageDeclaration().map(pd -> pd.getName().asString()).orElse("");

            MethodInfo methodInfo = new MethodInfo(nomMethode, declaringClass, packageName);

            Integer curCC = methodInfoList.getOrDefault(methodInfo, 0) + 1;
            methodInfoList.put(methodInfo, curCC);
        }

        @Override
        public void visit(TryStmt statement, Map<MethodInfo, Integer> methodInfoList) {
            super.visit(statement, methodInfoList);

            CompilationUnit current_cu = statement.findCompilationUnit().get();

            String declaringClass = current_cu.getPrimaryTypeName().get();
            String packageName = current_cu.getPackageDeclaration().map(pd -> pd.getName().asString()).orElse("");

            MethodInfo methodInfo = new MethodInfo(nomMethode, declaringClass, packageName);

            Integer curCC = methodInfoList.getOrDefault(methodInfo, 0) + 1;
            methodInfoList.put(methodInfo, curCC);
        }

        @Override
        public void visit(CatchClause statement, Map<MethodInfo, Integer> methodInfoList) {
            super.visit(statement, methodInfoList);

            CompilationUnit current_cu = statement.findCompilationUnit().get();

            String declaringClass = current_cu.getPrimaryTypeName().get();
            String packageName = current_cu.getPackageDeclaration().map(pd -> pd.getName().asString()).orElse("");

            MethodInfo methodInfo = new MethodInfo(nomMethode, declaringClass, packageName);

            Integer curCC = methodInfoList.getOrDefault(methodInfo, 0) + 1;
            methodInfoList.put(methodInfo, curCC);
        }

        @Override
        public void visit(WhileStmt statement, Map<MethodInfo, Integer> methodInfoList) {
            super.visit(statement, methodInfoList);

            CompilationUnit current_cu = statement.findCompilationUnit().get();

            String declaringClass = current_cu.getPrimaryTypeName().get();
            String packageName = current_cu.getPackageDeclaration().map(pd -> pd.getName().asString()).orElse("");

            MethodInfo methodInfo = new MethodInfo(nomMethode, declaringClass, packageName);

            Integer curCC = methodInfoList.getOrDefault(methodInfo, 0) + 1;
            methodInfoList.put(methodInfo, curCC);
        }
    }

    private record MethodInfo(String name, String declaringClass, String packageName) {

    }
}
```
