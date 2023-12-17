package fr.istic.vv;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for (TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        // Get all Field
        List<String> listField = new ArrayList<>();
        for (BodyDeclaration<?> field : declaration.getFields()) {
            String[] tmp = field.toString().replace(";", "").split(" ");
            if (tmp[0].equals("private")) {
                listField.add(tmp[tmp.length - 1]);
            }
        }
        for (BodyDeclaration<?> member : declaration.getMethods()) {
            for (String field : listField) {
                if (member.toString().toLowerCase().contains("get" + field)) {
                    listField.remove(field);
                    break;
                }
            }
        }

        try {
            FileWriter myWriter = new FileWriter("Rapport_noGetter.txt",true);
            myWriter.write("Class : " + declaration.getFullyQualifiedName().orElse("[Anonymous]")+"\n");
            for (String f : listField) {
                myWriter.write("Field : " + f+"\n");
            }
            myWriter.write("\n");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
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
