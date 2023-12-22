package fr.istic.vv;

import com.github.javaparser.Problem;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.SourceRoot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException {
        if(args.length < 1) {
            System.err.println("Should provide the path to the source code and the destination path to the report");
            System.exit(1);
        }

        File file = new File(args[0]);
        if(!file.exists() || !file.isDirectory() || !file.canRead()) {
            System.err.println("Provide a path to an existing readable directory");
            System.exit(2);
        }

        SourceRoot root = new SourceRoot(file.toPath());

        //print all public elements
        /* 
        PublicElementsPrinter printer = new PublicElementsPrinter();
        root.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> unit.accept(printer, null));
            return SourceRoot.Callback.Result.DONT_SAVE;
        });*/

        //print all private fields without public getter
        /*PrivateFieldsPrinter printer = new PrivateFieldsPrinter();
        root.parse("", (localPath, absolutePath, result) -> {
            //System.out.println(printer.getPrivateAttributesNamesWithoutPublicGetter());
            printer.generateHtmlReport(printer.getPrivateAttributesNamesWithoutPublicGetter(),args[1]);
            result.ifSuccessful(unit -> unit.accept(printer, null));
            return SourceRoot.Callback.Result.DONT_SAVE;
        });*/

        //Print cyclomatic complexity
        CyclomaticComplexityParser cycloParser = new CyclomaticComplexityParser();
        root.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> unit.accept(cycloParser, null));
            return SourceRoot.Callback.Result.DONT_SAVE;
        });
        System.out.println("==============================================");
        ReportMaker report = new ReportMaker(cycloParser.getCurrentInfo(), args[1]);
        report.histogramByPackage();
    }


}
