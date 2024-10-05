package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class NoGetters extends VoidVisitorWithDefaults<Void> {

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        PrintWriter writer = null;
        try {
            FileWriter filewriter = new FileWriter("report.txt",true);
            writer = new PrintWriter(filewriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for(FieldDeclaration attribut:declaration.getFields()) {
            String varName = attribut.getVariable(0).getName().toString();
            String getterVarName = "get" + varName.substring(0,1).toUpperCase() + varName.substring(1);

            boolean getting = false;

            for(MethodDeclaration methode:declaration.getMethods()) {
                if (getterVarName.equals(methode.getName().toString())) {
                    getting = true;
                }
            }

            writer.println(declaration.getFullyQualifiedName().toString() + " " + varName + " : " + getting);

        }
        writer.close();
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(EnumDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }
}