package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class CyclomaticComplexityVisitor extends VoidVisitorWithDefaults<Void> {
    public static String resultatTotal = "";
    public String resultat;
    private String className;
    private String methodName;
    private int cpt;
    private static int[] histogram;

    public CyclomaticComplexityVisitor() {
        resultat = "";
        className = "";
        methodName = "";
        cpt = 1; //compteur de complexité cyclomatique (nb de comditions + 1)
        histogram = new int[100];//taille arbitraire
    }
    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        className = declaration.getFullyQualifiedName().orElse("[Anonymous]");
        resultat += "\nClasse : " + className + "\n";

        //Parcours des méthodes
        for(MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }

        this.writeResult(); //on écrit notre résultat de la classe dans le doc global
        resultat = "";
        //parcours sous-classes
        for (BodyDeclaration<?> classes: declaration.getMembers()) {
            if (classes instanceof ClassOrInterfaceDeclaration) { //Si on rentre dans une classe on recrée un Visiteur et on refait le même traitement sur la classe fille
                classes.accept(this, null);
            }
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        //Quand on visite une méthode, on regarde tous ses membres et si on trouve des if, for, foreach, while on ajoute 1
        cpt = 1;
        methodName = declaration.getDeclarationAsString();
        if(declaration.getBody().isPresent()){
            for(Statement statement: declaration.getBody().get().getStatements()){
                   statement.accept(this, arg);
            }
        }
        resultat += methodName + " : " + cpt + "\n";
        histogram[cpt] += 1;
    }

    public void visit(IfStmt statement, Void arg) {
        cpt++;
        if(statement.hasThenBlock()){
            for(Node node : statement.getThenStmt().getChildNodes()){
                node.accept(this, arg);
            }
        }
        if(statement.hasElseBlock()){
            for(Node node : statement.getElseStmt().get().getChildNodes()){
                node.accept(this, arg);
            }
        }
    }
    public void visit(ForStmt statement, Void arg) {
        cpt++;
        if(!statement.hasEmptyBody()){
            for(Node node : statement.getBody().getChildNodes()){
                node.accept(this, arg);
            }
        }
    }

    public void visit(ForEachStmt statement, Void arg) {
        cpt++;
        if(!statement.hasEmptyBody()){
            for(Node node : statement.getBody().getChildNodes()){
                node.accept(this, arg);
            }
        }
    }

    public void visit(WhileStmt statement, Void arg) {
        cpt++;
        if(!statement.hasEmptyBody()){
            for(Node node : statement.getBody().getChildNodes()){
                node.accept(this, arg);
            }
        }
    }



        /**
         * Récupère les résultats du parser (les attributs) et
         */
    public void writeResult() {
        resultatTotal += resultat;
    }

    public void printHistogram(){
        System.out.println("Répartition des CC");
        int id_sup = 0;
        for(int i = 0; i <histogram.length; i++){
            if(histogram[i]!=0){
                id_sup = i;
            }
        }
        for(int i = 0; i <=id_sup; i++){
            System.out.print(i + " : ");
            for(int j = 0; j<histogram[i]; j++){
                System.out.print("*");
            }
            System.out.println();
        }
    }
}
