package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.util.ArrayList;
import java.util.List;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {

    List<String> public_method = new ArrayList<String>();
    List<String> private_attributs = new ArrayList<String>();

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if(!declaration.isPublic()) return;
        System.out.println(declaration.getFullyQualifiedName().orElse("[Anonymous]"));
        for(MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }
        // Printing nested types in the top level
        for(FieldDeclaration field : declaration.getFields()) {
                field.accept(this, arg);
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
        if(!declaration.isPublic()) return;
        System.out.println("  " + declaration.getDeclarationAsString(true, true));
        //we add the public method on the list of public method
        public_method.add(declaration.getDeclarationAsString(true,true));
    }
    
    public void attributs_that_dont_get_getter(){
        List<String> no_getter = new ArrayList<String>();
        for (int i =0; i<private_attributs.size();i++){
            String[] temp_attribut = private_attributs.get(i).split(" ");
                    boolean got_getter=false;
            for (int y=0;y<public_method.size();y++){
                String[] temp_methode=public_method.get(y).split(" ");
                if (temp_methode[2].startsWith("get")){
                    if ((temp_methode[2].substring(3,temp_methode[2].length()-2)).toLowerCase().equals(temp_attribut[2].toLowerCase().substring(0,temp_attribut[2].length()-1))){
                       got_getter=true;
                    }
                } else if (temp_methode[2].startsWith("is")) {
                    if ((temp_methode[2].substring(2,temp_methode[2].length()-2)).toLowerCase().equals(temp_attribut[2].toLowerCase().substring(0,temp_attribut[2].length()-1))){
                        got_getter=true;
                    }
                }
            }
            if(!got_getter){
            no_getter.add(private_attributs.get(i));}
        }
        System.out.println(no_getter);
        }
        public void visit(FieldDeclaration declaration,Void arg){
            if(!declaration.isPrivate()) return;
            System.out.println("  " + declaration.toString());
            private_attributs.add(declaration.toString());

        }
    }

