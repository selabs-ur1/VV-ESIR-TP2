# Code of your exercise

Put here all the code created for this exercise

```java
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Utilisation : java Main <chemin_vers_le_dossier_du_code>");
            System.exit(1);
        }

        String chemin_dossier = args[0];
        analyse(chemin_dossier);
    }

    private static void analyse(String sourcePath) {
        List<FieldInfo> listeDesAttributs = new ArrayList<>();

        try {
            File sourceFolder = new File(sourcePath);
            if (sourceFolder.isDirectory()) {
                for (File file : Objects.requireNonNull(sourceFolder.listFiles())) {
                    if (file.isFile() && file.getName().endsWith(".java")) {
                        CompilationUnit cu = StaticJavaParser.parse(file);

                        cu.accept(new FieldVisitor(), listeDesAttributs);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("listeDesAttributs = " + listeDesAttributs);

        genererRapport(listeDesAttributs, "rapport.csv");
    }

    private static class FieldVisitor extends VoidVisitorAdapter<List<FieldInfo>> {
        @Override
        public void visit(FieldDeclaration fieldDeclaration, List<FieldInfo> fieldInfoList) {
            super.visit(fieldDeclaration, fieldInfoList);

            // Vérifier si l'attribut est privé et s'il n'a pas de getter public
            if (fieldDeclaration.getModifiers().contains(Modifier.privateModifier())
                    && !hasPublicGetter(fieldDeclaration, fieldDeclaration.getVariables().get(0).getNameAsString())) {

                CompilationUnit current_cu = fieldDeclaration.findCompilationUnit().get();

                String fieldName = fieldDeclaration.getVariables().get(0).getNameAsString();
                String declaringClass = current_cu.getPrimaryTypeName().get();
                String packageName = current_cu.getPackageDeclaration().map(pd -> pd.getName().asString()).orElse("");

                fieldInfoList.add(new FieldInfo(fieldName, declaringClass, packageName));
            }
        }

        private boolean hasPublicGetter(FieldDeclaration fieldDeclaration, String fieldName) {
            String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            return fieldDeclaration.findCompilationUnit().get().getClassByName(getterName).isPresent();
        }
    }

    private static void genererRapport(List<FieldInfo> fieldInfoList, String outputFile) {
        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write("Attribut,Classe,Package\n");
            for (FieldInfo fieldInfo : fieldInfoList) {
                writer.write(fieldInfo.name + "," + fieldInfo.declaringClass + "," + fieldInfo.packageName + "\n");
            }
            System.out.println("Le rapport se nomme : " + outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private record FieldInfo(String name, String declaringClass, String packageName) {

    }
}
```
