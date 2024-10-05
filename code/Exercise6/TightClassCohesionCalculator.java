package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;
import com.github.javaparser.ast.stmt.SwitchEntry;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import static com.github.javaparser.ast.expr.BinaryExpr.Operator.AND;
import static com.github.javaparser.ast.expr.BinaryExpr.Operator.OR;

public class TightClassCohesionCalculator extends VoidVisitorWithDefaults<Void> {

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
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

    /**
     * Calculates the Tight Class Cohesion (TCC) between two methods 
     * by checking if they share any attributes from the given list 
     * of field declarations. 
     *
     * @param m1 The first method declaration to compare.
     * @param m2 The second method declaration to compare.
     * @param atbs A list of field declarations in the class, used to 
     *             determine shared attributes between the two methods.
     * @return true if both methods share at least one attribute; 
     *         otherwise, returns false.
     */
    public boolean hasSharedAttributes(MethodDeclaration m1, MethodDeclaration m2, List<FieldDeclaration> atbs) {
        Set<String> m1Attributes = new HashSet<>();
        m1.findAll(FieldAccessExpr.class).forEach(var -> m1Attributes.add(var.getNameAsString()));

        Set<String> m2Attributes = new HashSet<>();
        m2.findAll(FieldAccessExpr.class).forEach(var -> m2Attributes.add(var.getNameAsString()));

        for (FieldDeclaration attribut : atbs) {
            for (VariableDeclarator var : attribut.getVariables()) {
                String name = var.getName().toString();

                if (m1Attributes.contains(name) && m2Attributes.contains(name)) {
                    return true; // Return true if both methods access the same attribute
                }
            }
        }
        return false; // Return false if no common attributes are found
    }

    /**
     * Visits a type declaration (class or interface) and calculates 
     * its Tight Class Cohesion (TCC). It iterates through all method 
     * declarations within the type, compares them to count how many 
     * share attributes, and writes the TCC value to a report file. 
     *
     * @param declaration The type class or interface declaration being visited.
     * @param arg A generic argument.
     */
    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if (!declaration.isPublic()) return;

        PrintWriter writer = null;
        try {
            FileWriter filewriter = new FileWriter("report.txt", true);
            writer = new PrintWriter(filewriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<MethodDeclaration> lesMethodes = declaration.getMethods();
        List<FieldDeclaration> lesAttributs = declaration.getFields();

        float somme = 0;

        for (int i = 0; i < lesMethodes.size(); i++) {
            for (int j = i + 1; j < lesMethodes.size(); j++) {
                // Increment sum if the two methods share attributes
                if (hasSharedAttributes(lesMethodes.get(i), lesMethodes.get(j), lesAttributs)) {
                    somme++;
                }
            }
        }

        float N = lesMethodes.size(); // Total number of methods
        float NP = N * (N - 1) / 2.0f; // Calculate combinations of methods (N choose 2)

        writer.println(declaration.getFullyQualifiedName().toString() + " : " + somme / NP);

        // Visit inner types if any (e.g., nested classes)
        for (BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration)
                member.accept(this, arg);
        }
        writer.close();
    }
}