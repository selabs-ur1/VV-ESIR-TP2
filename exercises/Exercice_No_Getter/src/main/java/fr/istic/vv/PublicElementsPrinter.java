package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.util.List;
import java.util.ArrayList;

// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methodsName
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {

    private String test;

    public String getTest(){
        return this.test;
    }

    MyFileWriter fileWriter = new MyFileWriter("./output.txt");

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        // Ajoute le package et le nom de la classe
        fileWriter.appendContent("\n" + declaration.getFullyQualifiedName().orElse("[Anonymous]"));

        // Récupère tous les noms de méthode
        List<String> methodsName = new ArrayList<>();
        for(MethodDeclaration method : declaration.getMethods()){
            methodsName.add(method.getNameAsString());
        }

        // Récupère toutes les déclarations de variables.
        List<FieldDeclaration> privateFields = new ArrayList<>();
        for(FieldDeclaration field: declaration.getFields()){
            if(!field.isPublic()){
                privateFields.add(field);
            }
        }

        for(FieldDeclaration field: privateFields){
            boolean write = true;
            String fieldName = "";
            for(String method : methodsName){
                fieldName = field.getVariables().get(0).getNameAsString();
                // Vérifie s'il existe une méthode ayant pour nom get + le nom de la variable.
                if(method.toLowerCase().equals("get" + fieldName.toLowerCase())){
                    write = true;
                }
            }

            if(write){
                fileWriter.appendContent("  private " + fieldName + " has no public getter.");
            }
        }
    }
}
