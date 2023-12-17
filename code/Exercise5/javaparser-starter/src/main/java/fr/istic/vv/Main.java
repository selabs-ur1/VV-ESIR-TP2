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

        //create the file
        try {
            File myObj = new File("Rapport_CC.txt");
            if (myObj.createNewFile()) {
              System.out.println("File created: " + myObj.getName());
            } else {
              System.out.println("File already exists.");
            }
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
        //empty the file
        try {
            FileWriter myWriter = new FileWriter("Rapport_CC.txt");
            myWriter.write("");
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        

        SourceRoot root = new SourceRoot(file.toPath());
        PublicElementsPrinter printer = new PublicElementsPrinter();
        root.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> unit.accept(printer, null));
            return SourceRoot.Callback.Result.DONT_SAVE;
        });

        // write histograme
        int max_name = 0;
        for (String key : PublicElementsPrinter.histogram.keySet()) {
            max_name = Math.max(max_name, key.length());
        }
        try {
            FileWriter myWriter = new FileWriter("Rapport_CC.txt", true);
            myWriter.write("Histograme : " + "\n");
            for (String key : PublicElementsPrinter.histogram.keySet()) {
                int ecart = max_name-key.length();
                myWriter.write(key);
                for(int i = 0 ; i < ecart ; i++) myWriter.write(" ");
                myWriter.write(": ");
                for(int i = 0 ; i < PublicElementsPrinter.histogram.get(key) ; i++) myWriter.write("-");
                myWriter.write("\n");
            }
            myWriter.write("\n");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


}
