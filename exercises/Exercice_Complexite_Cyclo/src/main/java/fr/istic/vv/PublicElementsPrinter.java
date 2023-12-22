package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.util.List;
import java.util.ArrayList;

// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methodsName
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {

    private String test;

    public String getTest() {
        return this.test;
    }

    public static int max(int a, int b, int c) {
        if (a > b) {
            if (a > c) {
                return a;
            } else {
                return c;
            }
        } else {
            if (b > c) {
                return b;
            } else {
                return c;
            }
        }
    }

    MyFileWriter fileWriter = new MyFileWriter("./output.csv");

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for (TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        // Le package et le nom de la classe
        String packageAndClass = declaration.getFullyQualifiedName().orElse("[Anonymous]");

        // Récupère tous les noms de méthode
        for (MethodDeclaration method : declaration.getMethods()) {
            String methodName = method.getNameAsString();
            String parameterTypes = "";
            for (Parameter parameter : method.getParameters()) {
                parameterTypes += parameter.getTypeAsString() + " ";
            }

            if (parameterTypes.length() > 0) {
                parameterTypes = parameterTypes.substring(0, parameterTypes.length() - 1);
            }
            int complexite_cyclo = calculComplexiteCyclo(method);
            Main.complexites.add(new Double(complexite_cyclo));

            fileWriter
                    .appendContent(packageAndClass + ";" + methodName + ";" + parameterTypes + ";" + complexite_cyclo);
        }
    }

    private int calculComplexiteCyclo(MethodDeclaration method) {
        int complexite_cyclo = 1;
        complexite_cyclo += method.findAll(IfStmt.class).size();
        complexite_cyclo += method.findAll(SwitchStmt.class).size();
        complexite_cyclo += method.findAll(ForStmt.class).size();
        complexite_cyclo += method.findAll(WhileStmt.class).size();
        complexite_cyclo += method.findAll(DoStmt.class).size();
        return complexite_cyclo;
    }
}
