package fr.istic.vv;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.nodeTypes.NodeWithDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {

    public static Map<String, Integer> histogram = new HashMap<>();

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for (TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {

        String[] compare = { "if", "else", "for", "while" };

        // check CC for all methods
        for (BodyDeclaration<?> member : declaration.getMethods()) {
            int val = 1;
            for (String word : member.toString().split(" ")) {
                for (String cc : compare) {
                    if (word.equals(cc)) {
                        val++;
                    }
                }
            }
            String name = ((MethodDeclaration) member).getDeclarationAsString(false, false);
            histogram.put(declaration.getFullyQualifiedName().orElse("[Anonymous]") + " " + name, val);
            try {
                FileWriter myWriter = new FileWriter("Rapport_CC.txt", true);
                myWriter.write("Class : " + declaration.getFullyQualifiedName().orElse("[Anonymous]") + "\n");
                myWriter.write("Methode : " + name + "\n");
                myWriter.write("Cyclomantic Complexity : " + val + "\n");
                myWriter.write("\n");
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

            
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(EnumDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        if (!declaration.isPublic())
            return;
        System.out.println("  " + declaration.getDeclarationAsString(true, true));
    }

}
