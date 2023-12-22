package fr.istic.vv;

import com.github.javaparser.utils.SourceRoot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {
        if(args.length == 0) {
            System.err.println("Should provide the path to the source code");
            System.exit(1);
        }

        File file = new File(args[0]);
        if(!file.exists() || !file.isDirectory() || !file.canRead()) {
            System.err.println("Provide a path to an existing readable directory");
            System.exit(2);
        }


        File report = new File("report.txt");
        if (report.createNewFile()) {
            System.out.println("File created: " + report.getName());
        } else {
                System.out.println("File already exists.");
        }
        SourceRoot root = new SourceRoot(file.toPath());
        NoGetterVisitor printer = new NoGetterVisitor();
        NoGetterVisitor.resultatTotal += "Attributs privés de classes publiques sans getters \n";

        //Parcours des fichiers du dossier fourni
        root.parse("", (localPath, absolutePath, result) -> {
            NoGetterVisitor.resultatTotal += "\n========================================\nFichier : " + localPath + "\n========================================\n";
            result.ifSuccessful(unit -> unit.accept(printer, null)); //on accepte le visiteur et on commence le traitement
            return SourceRoot.Callback.Result.DONT_SAVE;
        });

        //Ecriture du résultat dans le rapport .txt
        try {
            FileWriter writer = new FileWriter(report);
            writer.write(NoGetterVisitor.resultatTotal);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
