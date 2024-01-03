package fr.istic.vv;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {
    private int cc = 1;
    private int cc_max = 12;
    private int[] cc_list = new int[cc_max];
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
        for(BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration)
                member.accept(this, arg);
        }
        for (int i = 0; i < cc_list.length; i++) {
            String histo = afficheHisto(cc_list[i]);
                    System.out.println(i+1 +" : "+ histo);
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

        System.out.println("  " + declaration.getDeclarationAsString(true, true));
        
       //Recuperer le body de la methode et extraire les différentes statements
        declaration.getBody().ifPresent(body -> {
            
            NodeList<Statement> statements = body.getStatements();
            
            for (Statement statement : statements) {
                visitBlock(statement);
            }
            //affichage complexité cyclomatique
            System.out.println("       Cyclomatic complexity :" + cc );

            //Incrémentater de 1 le nombre de methode qui ont une complexité  "cc"
            //Si cc =1 ; on incrémente le nombre de methodes de complexité 1
            for (int i = 0; i < cc_max; i++) {
                if (cc == i+1){
                    cc_list[i] += 1;
                }
            }

            //Reinitialiser la valeur de la cc
             cc = 1; 
        });
        
    }

    public void visitBlock(Statement statement) {
        //Condition pour indiquer les types de statements qu'on doit eviter
        boolean stmtToConsider = statement.isBlockStmt() | statement.isIfStmt() | statement.isForEachStmt() | statement.isWhileStmt() | statement.isForEachStmt() | statement.isSwitchStmt();
        if (!stmtToConsider) {}

        if (statement.isBlockStmt()){
            NodeList<Statement> statements = statement.asBlockStmt().getStatements();
            for (Statement stmt : statements) {
                visitStatement(stmt);
            }
        }
        else{
            visitStatement(statement);
        }
    }

    public void visitStatement(Statement stmnt) {
        if (stmnt.isIfStmt()) {
            visitIf(stmnt.asIfStmt());
        }
        else if (stmnt.isForStmt()) {
            visitFor(stmnt.asForStmt());
        }
        else if (stmnt.isWhileStmt()) {
            visitWhile(stmnt.asWhileStmt());           
        } 
        else if (stmnt.isSwitchStmt()) {
            visitSwitch(stmnt.asSwitchStmt());
        }

    }

    public void visitIf(IfStmt ifstmt) {
        cc +=1;
        if (ifstmt.hasThenBlock()) {
            Statement thenstmt = ifstmt.getThenStmt();
            NodeList<Statement> thenStatements = thenstmt.asBlockStmt().getStatements(); 
            for (Statement thenStatement : thenStatements) {
                visitBlock(thenStatement);
            } 
        }
        //On a une 'else' à la suite du if
        if (ifstmt.hasElseBranch()) {
            //Récupère le else
            Statement elseStmt =ifstmt.getElseStmt().get();
            if (elseStmt != null) {
                visitBlock(elseStmt);
            }
            
        }

    }

    public void visitWhile(WhileStmt whileStmt) {
        cc +=1; 
        visitBlock(whileStmt.getBody());
    }

    public void visitFor(ForStmt forStmt) {
        cc +=1;
        visitBlock(forStmt.getBody());
    }

    public void visitSwitch(SwitchStmt switchStmt){
        for (Node entry : switchStmt.getEntries()) {
            cc +=1;
        }
        cc -=1;

    }

    //Affichage histogramme
    public String afficheHisto(int cc) {
        String histo = ".".repeat(cc);
        return histo;
    }

  


}
        


