package nogetter.nogetter;

/**
 * Hello world!
 *
 */

import com.github.javaparser.Problem;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.SourceRoot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class App 
{
    public static void main( String[] args ) throws IOException
    {	
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
        PublicElementsPrinter2 printer = new PublicElementsPrinter2();        
        
        root.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> unit.accept(printer, null));
            return SourceRoot.Callback.Result.DONT_SAVE;
        });
        
        extraire(file.getAbsolutePath() + "\\File_analyse_result_.txt", printer.getOutput());
    }
    
 // ---- Méthode pour extraire le résultat d'un analyse
 	public static void extraire(String lien, String texte) {
 		Path path = Paths.get(lien);
 		try {
 			byte[] bs = texte.getBytes();
 			Files.write(path, bs);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
 	}
}
