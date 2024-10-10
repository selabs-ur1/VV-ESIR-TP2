package fr.istic.vv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import com.github.javaparser.utils.SourceRoot;


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
            CyclomaticComplexityVisitor visitor = new CyclomaticComplexityVisitor();
            
            root.parse("", (localPath, absolutePath, result) -> {
                result.ifSuccessful(unit -> unit.accept(visitor, null));
                return SourceRoot.Callback.Result.DONT_SAVE;
            });
    
            generateReport(visitor.getMethodComplexityMap());
            
        }
    
        private static void generateReport(Map<String, CyclomaticComplexityVisitor.MethodInfo> methodComplexityMap) throws IOException {
            FileWriter writer = new FileWriter("cyclomatic_complexity_report.txt");
            
            writer.write("Class.Method\tCC\n");
            for (CyclomaticComplexityVisitor.MethodInfo info : methodComplexityMap.values()) {
                writer.write(info.className + "." + info.methodName + "\t" + info.cyclomaticComplexity + "\n");
            }
            
            writer.close();
        }
}
