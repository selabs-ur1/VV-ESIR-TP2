package fr.istic.vv;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.utils.SourceRoot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

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

        SourceRoot root = new SourceRoot(file.toPath());
        PublicElementsPrinter printer = new PublicElementsPrinter();
        root.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> unit.accept(printer, null));
            return SourceRoot.Callback.Result.DONT_SAVE;
        });

        // Initializing detectedFields for the exercice
        String noGetter = "";
        String CyclomaticComplexityJP = "";

        // Iterating variable for class names
        int i = 0;

        // Looping through all private variables without getter
        for (FieldDeclaration field : printer.getPrivateVarWithoutGetter()) {
            noGetter += 
            
            // Adding private variable name
            "Private variable : " + field.getVariable(0).getName() +

            // Adding class name of each variable
             " | Class : " + printer.getClassNames().get(i)+

            // Adding package name
            " | Package name : " + printer.getPackageNames()  +"\n";
            i++;
        }

        i = 0;
        for(MethodDeclaration method : printer.getMethod()){
            CyclomaticComplexityJP += 
            // Adding package name
            " | Package name : " + printer.getPackageNames() +

            // Adding class name of each variable
             " | Class : " + printer.getClassNamesMethods().get(i)+

            // Adding method name
            " | Method name : " + method.getNameAsString() +

            // Adding cyclomatic complexity
            " | Cyclomatic complexity : " + printer.getCyclomaticComplexity()[i];

            if((printer.getParameters().size())>i){
                // Adding method parameters
                CyclomaticComplexityJP += " | Parameters : " + printer.getParameters().get(i)  + "\n";
            }
            i++;
        }


        // Create a HashMap to store the frequency of each cyclomatic complexity value
        HashMap<Integer, Integer> histogram = new HashMap<>();
        int max = 0;
        // Loop through the cyclomatic complexity values
        for (int complexity : printer.getCyclomaticComplexity()) {
            // Increment the count for the current complexity value
            histogram.put(complexity, histogram.getOrDefault(complexity, 0) + 1);
        }

        System.out.println("Cyclomatic complexity histogram:");

        // Find the maximum frequency
        for (int complexity : histogram.keySet()) {
            int frequency = histogram.get(complexity);
            if(frequency > max){
                max = frequency;
            }
        }

        

        // Print the histogram
        for (int complexity : histogram.keySet()) {
            int frequency = histogram.get(complexity);
            if(complexity < 10){
                System.out.print(" ");
            }
            System.out.print(complexity);
            for(int j = 0; j < frequency; j++){
                System.out.print("=");
            }
            System.out.println();
        }

        

          // Writing txt file
          try {

            // Instance of file writer
            FileWriter myWriter = new FileWriter("noGetter.txt");
            FileWriter CyclomaticWriter = new FileWriter("CyclomaticComplexity.txt");

            // Writing to file
            myWriter.write(noGetter);
            CyclomaticWriter.write(CyclomaticComplexityJP);
            myWriter.close();
            CyclomaticWriter.close();
            System.out.println("Successfully wrote to the files.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        
    }


}
