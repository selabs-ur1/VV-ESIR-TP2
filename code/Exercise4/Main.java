package fr.istic.vv;

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

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java FieldExtractor <path_to_source_code>");
            System.exit(1);
        }

        String sourcePath = args[0];
        List<FieldInfo> fields = extractFields(sourcePath);

        // Envoie les résultats dans le fichier report.csv 
        generateReport(fields, "report.csv");
    }

    private static List<FieldInfo> extractFields(String sourcePath) {
        List<FieldInfo> fields = new ArrayList<>();

        try {
            File projectDir = new File(sourcePath);

            if (!projectDir.isDirectory()) {
                System.err.println("Emplacement du repertoire introuvable");
                System.exit(1);
            }

            // Visite tous les fichiers Java du repertoire 
            for (File file : projectDir.listFiles((dir, name) -> name.endsWith(".java"))) {
                CompilationUnit cu = StaticJavaParser.parse(file);

                // Utilise un Visiteur pour traverser l'AST et extraire les informations des methodes
                cu.accept(new FieldVisitor(fields, file.getPath()), null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fields;
    }

    private static void generateReport(List<FieldInfo> fields, String outputPath) {
        try (FileWriter writer = new FileWriter(outputPath)) {
            // Ecrit l'entete
            writer.write("Field Name,Declaring Class,Package\n");

            // Ecrit le contenu
            for (FieldInfo field : fields) {
                writer.write(field.getName() + "," + field.getDeclaringClass() + "," + field.getPackage() + "\n");
            }

            System.out.println("Rapport genere correctement: " + outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Classe pour contenir les informations des methodes
    private static class FieldInfo {
        private final String name;
        private final String declaringClass;
        private final String packageName;

        public FieldInfo(String name, String declaringClass, String packageName) {
            this.name = name;
            this.declaringClass = declaringClass;
            this.packageName = packageName;
        }

        public String getName() {
            return name;
        }

        public String getDeclaringClass() {
            return declaringClass;
        }

        public String getPackage() {
            return packageName;
        }
    }

    // Visiteur qui extrait les informations des methodes
    private static class FieldVisitor extends VoidVisitorAdapter<Void> {
        private final List<FieldInfo> fields;
        private final String filePath;

        public FieldVisitor(List<FieldInfo> fields, String filePath) {
            this.fields = fields;
            this.filePath = filePath;
        }

        @Override
        public void visit(FieldDeclaration fieldDeclaration, Void arg) {
            // Regarde si le champs est privé et s'il n'a pas de public getter
            if (fieldDeclaration.getModifiers().contains(com.github.javaparser.ast.Modifier.privateModifier()) &&
                    !hasPublicGetter(fieldDeclaration, fieldDeclaration.getVariables().get(0).getNameAsString())) {
                String fieldName = fieldDeclaration.getVariables().get(0).getNameAsString();
                String declaringClass = fieldDeclaration.findAncestor(com.github.javaparser.ast.body.ClassOrInterfaceDeclaration.class)
                        .map(classDeclaration -> classDeclaration.getNameAsString()).orElse("");
                String packageName = declaringClass.isEmpty() ? "" : getPackageName(filePath);

                fields.add(new FieldInfo(fieldName, declaringClass, packageName));
            }

            super.visit(fieldDeclaration, arg);
        }

        private boolean hasPublicGetter(FieldDeclaration fieldDeclaration, String fieldName) {
            String getterMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            return fieldDeclaration.findCompilationUnit()
                    .flatMap(cu -> cu.getClassByName(fieldDeclaration.findAncestor(com.github.javaparser.ast.body.ClassOrInterfaceDeclaration.class)
                            .map(classDeclaration -> classDeclaration.getNameAsString()).orElse("")))
                    .map(classOrInterfaceDeclaration ->
                            classOrInterfaceDeclaration.getMethods().stream()
                                    .filter(methodDeclaration ->
                                            methodDeclaration.getModifiers().contains(Modifier.publicModifier()) &&
                                                    methodDeclaration.getNameAsString().equals(getterMethodName) &&
                                                    methodDeclaration.getParameters().isEmpty() &&
                                                    methodDeclaration.getType().equals(fieldDeclaration.getCommonType()))
                                    .findAny()
                                    .isPresent())
                    .orElse(false);
        }

        private String getPackageName(String filePath) {
            String[] parts = filePath.split(File.separator.equals("\\") ? "\\\\" : File.separator);
            StringBuilder packageName = new StringBuilder();

            for (String part : parts) {
                if (part.equals("src")) {
                    break;
                }
                if (packageName.length() > 0) {
                    packageName.append(".");
                }
                packageName.append(part);
            }

            return packageName.toString();
        }
    }
}
