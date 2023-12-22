package fr.istic.vv;


import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import fr.istic.vv.VisitorCC.FieldInfo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Main {
     public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java FieldExtractor <path_to_source_code>");
            System.exit(1);
        }
        VisitorCC visiteur = new VisitorCC();
        List<FieldInfo> fields = extractFields(args[0],visiteur);

        // Envoie les résultats dans le fichier report.csv 
        generateReport(fields, "fichierCC.csv");

        // Affichage de l'histogramme
        visiteur.afficheHistogramme();
    }

    // Methode permettant d'extraire les donnees
    private static List<FieldInfo> extractFields(String sourcePath, VisitorCC visiteur) {

        try {
            File projectDir = new File(sourcePath);

            if (!projectDir.isDirectory()) {
                System.err.println("Emplacement du repertoire introuvable");
                System.exit(1);
            }

            // Visite tous les fichiers Java du repertoire 
            for (File file : projectDir.listFiles((dir, name) -> name.endsWith(".java"))) {
                CompilationUnit cu = StaticJavaParser.parse(file);

                // Utilise un Visiteur pour traverser pour calculer la complexite cyclomatique
                cu.accept(visiteur, null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return visiteur.getFields();
    }
    // Methode permettant de generer le rapport
    private static void generateReport(List<FieldInfo> fields, String outputPath) {
        try (FileWriter writer = new FileWriter(outputPath)) {
            // Ecrit l'entete
            writer.write("Field Name,Declaring Class,Package,CyclomaticComplexity\n");

            // Ecrit le contenu
            for (FieldInfo field : fields) {
                writer.write(field.getName() + "," + field.getDeclaringClass() + "," + field.getPackage() + ","+field.getCyclomaticComplexity()+"\n");
            }

            System.out.println("Rapport genere correctement: " + outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
