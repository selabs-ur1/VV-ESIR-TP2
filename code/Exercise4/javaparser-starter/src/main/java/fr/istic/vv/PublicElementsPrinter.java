package fr.istic.vv;

import java.util.ArrayList;
import java.util.regex.*;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {
    //Liste des methodes d'une classe
    ArrayList<String> methods = new ArrayList<String>();
    boolean fieldAttented = false;
    
    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if(!declaration.isPublic()) return;
        
        for(MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }
        
        //getting fields
        for(FieldDeclaration field : declaration.getFields()) {
            
            field.accept(this, arg);
            if (fieldAttented) {
                //Nom du package avec des données parasites
                String strPackage = declaration.findCompilationUnit().map(cu -> cu.getPackageDeclaration()).toString();

                if (strPackage.indexOf("package")>0) {
                    //Nom du package sans parasite
                    String nomPackage = strPackage.substring(strPackage.indexOf("package")+8, strPackage.indexOf(";"));
                    System.out.println("\t Package : " + nomPackage);
                }
                
                System.out.println("\t Classe : " + declaration.getNameAsString());
                fieldAttented = false;
            }
        }

        // Printing nested types in the top level
        for(BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration)
                member.accept(this, arg);
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

        //Ajouter le nom de cahque methode à la liste de méthodes "methods" 
        methods.add(declaration.getNameAsString());
    }

    //Overriding visit field's method
    @Override
    public void visit(FieldDeclaration declaration, Void arg) {
        if(declaration.isPublic()) return;
        //Récuperer le champs privé
        String fields = declaration.getVariables().toString();
        //Extraire le nom du champs
        fields = fields.substring(1, fields.length()-1);
        for (String method : methods) {
            if (method.matches("get[A-Z]"+fields.substring(1))) {
                System.out.println(fields);
                fieldAttented = true;
            }
            
        }
        

    }
   


}
