package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;
import com.github.javaparser.ast.stmt.SwitchEntry;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import static com.github.javaparser.ast.expr.BinaryExpr.Operator.AND;
import static com.github.javaparser.ast.expr.BinaryExpr.Operator.OR;

public class CyclomaticComplexityPrinter extends VoidVisitorWithDefaults<Void> {

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if(!declaration.isPublic()) return;

        PrintWriter writer = null;
        try {
            FileWriter filewriter = new FileWriter("report.txt",true);
            writer = new PrintWriter(filewriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for(MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
            int val = calculateCyclomaticComplexity(method);

            writer.println(declaration.getFullyQualifiedName().toString() + " " + method.getName() + " : " + val);

        }
        for(BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration)
                member.accept(this, arg);
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

    /**
     * Calculates the Cyclomatic Complexity (CC) of a given method.
     *
     * According to the Mc Cabe method, this metric depends on the number of :
     * If Statements, For Statements, While Statements, Do Statements, Switch Entries and Binary Expressions
     *
     * It is calculated by summing the counts of all the structures mentionned above and adding 1,
     * which represents the default path taken when the method starts executing. 
     * 
     * @param method The MethodDeclaration object representing the method whose Cyclomatic Complexity is to be calculated.
     * @return The Cyclomatic Complexity value as an integer.
     */
    public int calculateCyclomaticComplexity(MethodDeclaration method) {

        List<IfStmt> ifStmts = method.getNodesByType(IfStmt.class);
        List<ForStmt> forStmts = method.getNodesByType(ForStmt.class);
        List<WhileStmt> whileStmts = method.getNodesByType(WhileStmt.class);
        List<DoStmt> doStmts = method.getNodesByType(DoStmt.class);
        List<SwitchEntry> catchStmts = method.findAll(SwitchEntry.class).stream()
                .filter(s -> s.getLabels().isNonEmpty()) // Ne pas inclure les déclarations "default", seulement les cas étiquetés
                .collect(Collectors.toList());
        List<ConditionalExpr> ternaryExprs = method.getNodesByType(ConditionalExpr.class);
        List<BinaryExpr> andExprs = method.getNodesByType(BinaryExpr.class).stream().
                filter(f -> f.getOperator() == AND).collect(Collectors.toList());
        List<BinaryExpr> orExprs = method.getNodesByType(BinaryExpr.class).stream().
                filter(f -> f.getOperator() == OR).collect(Collectors.toList());

        Integer total = ifStmts.size() +
                forStmts.size() +
                whileStmts.size() +
                doStmts.size() +
                catchStmts.size() +
                ternaryExprs.size() +
                andExprs.size() +
                orExprs.size() +
                1;
        return total;

    }
}