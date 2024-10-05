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


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class TightClassCohesionCalculator extends VoidVisitorWithDefaults<Void> {

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

        List<MethodDeclaration> lesMethodes = declaration.getMethods();
        List<FieldDeclaration> lesAttributs = declaration.getFields();

        float somme = 0;

        for(int i = 0 ; i < lesMethodes.size(); i ++) {
            for(int j = i+1 ; j < lesMethodes.size(); j ++) {
                if(test(lesMethodes.get(i), lesMethodes.get(j), lesAttributs)){
                    somme++;
                }
            }
        }

        float N = lesMethodes.size();
        float NP = N*(N-1)/2.0f;

        writer.println(declaration.getFullyQualifiedName().toString() + " : " + somme / NP);

        // Printing nested types in the top level
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

    public boolean test(MethodDeclaration m1,MethodDeclaration m2, List<FieldDeclaration> atbs) {
        Set<String> m1Attributes = new HashSet<>();
        m1.findAll(FieldAccessExpr.class).forEach(var -> m1Attributes.add(var.getNameAsString()));

        Set<String> m2Attributes = new HashSet<>();
        m2.findAll(FieldAccessExpr.class).forEach(var -> m2Attributes.add(var.getNameAsString()));

        for (FieldDeclaration attribut : atbs){
            for (VariableDeclarator var : attribut.getVariables()) {
                String name = var.getName().toString();

                if (m1Attributes.contains(name) && m2Attributes.contains(name)) {
                    return true;
                }
            }
        }
        return false;
    }
}