package fr.istic.vv;

import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.util.ArrayList;
import java.util.List;

// Classe Visitor permettant de calculer la Complecite Cyclomatique
public class VisitorCC extends VoidVisitorWithDefaults<Void> { 

    private List<FieldInfo> fields;
    private int[] histogramme;
    private String classe;
    private int nbNoeud;

    // Constructeur de la classe
    public VisitorCC() {
        fields = new ArrayList<>();
        histogramme = new int[200];
    }

    
    // Fonction qui visite les classes
    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        classe = declaration.getFullyQualifiedName().orElse("[Anonymous]");

        declaration.getMembers().forEach(member -> {
            if (member instanceof ClassOrInterfaceDeclaration) {
                member.accept(this, null);
            }
        });

        declaration.getMethods().forEach(method -> method.accept(this, arg));
    }
    // Fonction qui visite les fichiers
    @Override
    public void visit(CompilationUnit unit, Void arg) {
        unit.getTypes().forEach(type -> type.accept(this, null));
    }
    // Fonction qui visite les sous-classes
    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }
    // Fonction qui visite les methodes
    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
         
        String methode_name= declaration.getDeclarationAsString();
        nbNoeud = 1; 
        if (declaration.getBody().isPresent()) {
            declaration.getBody().get().getStatements().forEach(statement -> statement.accept(this, arg));
        }

        fields.add(new FieldInfo(methode_name, classe, "", nbNoeud));
        histogramme[nbNoeud] = histogramme[nbNoeud] + 1;
    }

   
    // Fonction qui visite les boucles Foreach
    public void visit(ForEachStmt statement, Void arg) {
        statement.getBody().getChildNodes().forEach(node -> node.accept(this, arg));
    }
   
    // Fonction qui visite les boucles For
    public void visit(ForStmt statement, Void arg) {
        statement.getBody().getChildNodes().forEach(node -> node.accept(this, arg));
    }
    
    // Fonction qui visite les boucles While
    public void visit(WhileStmt statement, Void arg) {
        statement.getBody().getChildNodes().forEach(node -> node.accept(this, arg));
    }
    
    // Fonction qui visite les If
    public void visit(IfStmt statement, Void arg) {
        nbNoeud =nbNoeud+1;
        if (statement.hasElseBlock()) {
            nbNoeud =nbNoeud+1;
            statement.getElseStmt().get().getChildNodes().forEach(node -> node.accept(this, arg));
        }
    }

    public void afficheHistogramme() {
        boolean found = false;
        String output = "";
        String si = "";

        for (int i = histogramme.length - 1; i > -1; i--) {
            si = i + " :";

            if (histogramme[i] != 0 || found) {
                found = true;
                for (int j = 0; j < histogramme[i]; j++) {
                    si = si + "[]";
                }
                output = si + "\n" + output;
            }
        }
        System.out.println(output);
    }

    public List<FieldInfo> getFields() {
        return fields;
    }
    // Classe contenant les informations de chaque methode
    public static class FieldInfo {
        private final String name;
        private final String declaringClass;
        private final String packageName;
        private final int cyclomaticComplexity;

        public FieldInfo(String name, String declaringClass, String packageName, int cyclomaticComplexity) {
            this.name = name;
            this.declaringClass = declaringClass;
            this.packageName = packageName;
            this.cyclomaticComplexity = cyclomaticComplexity;
        }

        public String getName() {
            return name;
        }

        public String getDeclaringClass() {
            return declaringClass;
        }

        public String getPackage() {
            return packageName;
        }

        public int getCyclomaticComplexity() {
            return cyclomaticComplexity;
        }
    }
}
