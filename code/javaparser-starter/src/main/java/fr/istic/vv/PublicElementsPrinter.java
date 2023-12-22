package fr.istic.vv;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {

    private List<String> report = new ArrayList<>();
    List<String>publicMethods=new ArrayList<>();
    List<String>fielsTab=new ArrayList<>();
    List<Integer>nbsCyclomatiqueTab=new ArrayList<>();
=======
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {
    List<String>publicMethods=new ArrayList<>();
    List<String>fielsTab=new ArrayList<>();
>>>>>>> 2e0a3dbc148e0ab91c07c1e24d5e70a357217feb
    String retour="";
    String retour1="";
    
    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for (TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if (!declaration.isPublic())
            return;
        System.out.println(declaration.getFullyQualifiedName().orElse("[Anonymous]"));
        for (MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }
        // Printing nested types in the top level
        for (BodyDeclaration<?> member : declaration.getMembers()) {
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
    public void visit(FieldDeclaration fieldDeclaration, Void arg) {
        String fieldName=fieldDeclaration.getVariable(0).getNameAsString();
        if (!fieldDeclaration.isPublic()) {
            fielsTab.add(fieldName);
            for(int j=0;j<publicMethods.size();j++){
                if(publicMethods.get(j).equals("get"+ fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1))){
                   
                    retour1 += ("Private Field: " + fieldName +
                        " in class: "
                        + fieldDeclaration.findAncestor(ClassOrInterfaceDeclaration.class).orElse(null)
                                .getNameAsString()
                        +
                        " in package: " + fieldDeclaration.findAncestor(CompilationUnit.class).orElse(null)
                                .getPackageDeclaration().map(p -> p.getNameAsString()).orElse("[Default Package]"))+" has public getter\n\t";
                    
                }    
            }
         
        }
        else
            System.out.println("There is not private fileds in this file!");

        }


<<<<<<< HEAD
   @Override
   public void visit(MethodDeclaration declaration, Void arg) {
    super.visit(declaration, arg);

    // Calculate Cyclomatic Complexity for the method
    int cyclomaticComplexity=calculateCyclomaticComplexity(declaration);
    if(calculateCyclomaticComplexity(declaration)> 1){
        cyclomaticComplexity=calculateCyclomaticComplexity(declaration)-1;
    }
    
    
    // Collect information for the report
    String packageName = declaration.findCompilationUnit()
            .flatMap(unit -> unit.getPackageDeclaration().map(PackageDeclaration::getNameAsString))
            .orElse("[Default Package]");

    String className = declaration.findAncestor(ClassOrInterfaceDeclaration.class)
            .flatMap(classOrInterface -> classOrInterface.getFullyQualifiedName())
            .orElse("[Anonymous]");

    String methodName = declaration.getNameAsString();
    String parameters = declaration.getParameters().toString();

    // Add entry to the report
    String entry = String.format("%s\t%s\t%s\t%s\t%d", packageName, className, methodName, parameters, cyclomaticComplexity);
    if (!report.contains(entry)) {
        nbsCyclomatiqueTab.add(cyclomaticComplexity);
        report.add(entry);
    }
}

    
private int calculateCyclomaticComplexity(MethodDeclaration method) {
    // Using an array to make the variable effectively final
    final int[] complexity = {1}; // Starting with 1 for the method itself

    // Add 1 for each if, while, for, and case
    method.walk(node -> {
        if (node instanceof IfStmt || node instanceof WhileStmt || node instanceof ForStmt) {
            System.out.println("Found a decision point");
            complexity[0]++;
        } else if (node instanceof SwitchStmt) {
            System.out.println("Found a switch statement");
            // Add 1 for each case in the switch statement
            SwitchStmt switchStmt = (SwitchStmt) node;
            System.out.println("Number of cases: " + switchStmt.getEntries().size());
            complexity[0] += switchStmt.getEntries().size();
        }
    });

     // Add 1 for each if, while, for, and case
     method.walk(node -> {
        if (node instanceof IfStmt || node instanceof WhileStmt || node instanceof ForStmt) {
            System.out.println("Found a decision point");
            complexity[0]++;
        } else if (node instanceof SwitchStmt) {
            System.out.println("Found a switch statement");
            // Add 1 for each case in the switch statement
            SwitchStmt switchStmt = (SwitchStmt) node;
            System.out.println("Number of cases: " + switchStmt.getEntries().size());
            complexity[0] += switchStmt.getEntries().size();
        }
    });

    // Add 1 for each logical OR and AND
    method.walk(BinaryExpr.class, expr -> {
        if (expr.getOperator() == BinaryExpr.Operator.OR || expr.getOperator() == BinaryExpr.Operator.AND) {
            System.out.println("Found a binary expression");
            complexity[0]++;
        }
    });


    System.out.println("Cyclomatic Complexity: " + complexity[0]);

    return complexity[0];
}

    public List<String> getReport() {
        System.out.println(nbsCyclomatiqueTab);
        return report;
=======
    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        if (!declaration.isPublic())
            return;
        else{
        publicMethods.add(declaration.getNameAsString());
        //System.out.println(publicMethods.toString() );
        }
    }

    public String getReport() {
    retour+="this is the list of private fields"+fielsTab.toString()+",only:\n\t"+retour1; 
        return retour;
>>>>>>> 2e0a3dbc148e0ab91c07c1e24d5e70a357217feb
    }

    /*public String getReport() {
    retour+="this is the list of private fields"+fielsTab.toString()+",only:\n\t"+retour1; 
        return retour;
    }*/

}
