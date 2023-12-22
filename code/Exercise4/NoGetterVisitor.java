package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.io.File;
import java.util.HashSet;
import java.util.Set;


//Cette classe visite un fichier java et stocke les attributs privés sans getters des classes publiques
public class NoGetterVisitor extends VoidVisitorWithDefaults<Void> {
    public static String resultatTotal = ""; //la string du rapport final
    public String resultat; //la string du rapport local à une classe
    private Set<String> getters; //stocke les noms les méthodes publiques
    private Set<String> attributes; //stocke les noms des attributs privés
    private String className; //stocke le nom du package et de la classe étudiée

    public NoGetterVisitor() {
        getters = new HashSet<>();
        attributes = new HashSet<>();
        className = "";
        resultat = "";
    }
    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            getters = new HashSet<>();
            attributes = new HashSet<>();
            className = "";
            resultat = "";
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if(!declaration.isPublic()) return; //si la classe n'est pas publique on ne l'analyse pas
        className = declaration.getFullyQualifiedName().orElse("[Anonymous]"); //on récupère le nom de la classe (et du package)
        resultat += "\n" + className + "\n";

        //Parcours des attributs
        for(FieldDeclaration field : declaration.getFields()) {// FieldDeclaration comprend la déclaration des champs de la classe (attributs)
            field.accept(this, arg);
        }

        //Parcours des méthodes
        for(MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }

        // On va retourner uniquement les attributs privés qui n'ont pas de getters
        for(String attribute : attributes) {
            String method = "get"+attribute+"()";
            if (!getters.contains(method)) {
               resultat += " - " + attribute + "\n";
            }
        }
        this.writeResult(); //on écrit notre résultat de la classe dans le doc global

        for (BodyDeclaration<?> classes: declaration.getMembers()) {
            if (classes instanceof ClassOrInterfaceDeclaration) { //Si on rentre dans une classe on refait le même traitement sur la classe fille
                getters = new HashSet<>();
                attributes = new HashSet<>();
                className = "";
                resultat = "";
                classes.accept(this, arg);
            }
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    public void visit(FieldDeclaration declaration, Void arg) { //récupère le nom de l'attribut privé
        String[] mots = declaration.toString().split(" ");
        if(mots[0].equals("private")) {
            mots[2] = mots[2].substring(0, mots[2].length() - 1);
            attributes.add(mots[2]);
        }
    }

    @Override
    public void visit(MethodDeclaration declaration, Void arg) { //récupère les méthodes publiques
        if(!declaration.isPublic()) return; //si la méthode est privée on ne la retient pas
        String[] mots = declaration.getDeclarationAsString(true, true).split(" ");
        getters.add(mots[2].toLowerCase());
    }

    /**
     * Concatène le résultat local à une classe au résultat global
     */
    public void writeResult() {
        System.out.println("Writing...");
        resultatTotal += resultat;
    }
}
