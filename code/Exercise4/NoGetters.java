package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class NoGetters extends VoidVisitorWithDefaults<Void> {

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    /**
     * Identify private fields that do not have a corresponding public getter method.
     * Constructs the expected getter name for each field and
     * checks if a matching public method exists in the class.
     *
     * If the method has a getter, then it is marked "true" in the report, else "false".
     *
     * @param declaration The class or interface declaration being visited.
     * @param arg A generic argument.
     */
    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        PrintWriter writer = null;
        try {
            FileWriter filewriter = new FileWriter("report.txt", true);
            writer = new PrintWriter(filewriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for(FieldDeclaration field : declaration.getFields()) {
            String varName = field.getVariable(0).getName().toString();  // Get the name of the field
            // Construct the expected getter method name following the Java convention (e.g., getAge for 'age')
            String getterVarName = "get" + varName.substring(0,1).toUpperCase() + varName.substring(1);

            boolean hasGetter = false;

            // Check if there is a public method with the expected getter name
            for(MethodDeclaration method : declaration.getMethods()) {
                if (getterVarName.equals(method.getName().toString())) {
                    hasGetter = true;  // Mark if the getter is found
                }
            }

            writer.println(declaration.getFullyQualifiedName().toString() + " " + varName + " : " + hasGetter);
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