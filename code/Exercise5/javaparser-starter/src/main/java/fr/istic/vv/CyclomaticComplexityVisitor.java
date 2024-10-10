package fr.istic.vv;

import java.util.HashMap;
import java.util.Map;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class CyclomaticComplexityVisitor extends VoidVisitorAdapter<Void> {
    private final Map<String, MethodInfo> methodComplexityMap = new HashMap<>();

    @Override
    public void visit(MethodDeclaration method, Void arg) {
        super.visit(method, arg);
        
        String methodName = method.getNameAsString();
        String className = method.findAncestor(ClassOrInterfaceDeclaration.class)
            .map(ClassOrInterfaceDeclaration::getNameAsString)
            .orElse("Unknown");
        
        // Obtenir les types de paramètres sous forme de chaîne séparée par des virgules
        String parameterTypes = method.getParameters().stream()
            .map(param -> param.getType().toString())
            .reduce((a, b) -> a + ", " + b)
            .orElse("");
        
        int cyclomaticComplexity = calculateCyclomaticComplexity(method);
        
        methodComplexityMap.put(methodName, new MethodInfo(className, methodName, parameterTypes, cyclomaticComplexity));
    }

    private int calculateCyclomaticComplexity(MethodDeclaration method) {
        int complexity = 1; // Démarre avec 1 pour la méthode elle-même
        complexity += method.findAll(com.github.javaparser.ast.expr.BinaryExpr.class).size();
        complexity += method.findAll(com.github.javaparser.ast.stmt.IfStmt.class).size();
        complexity += method.findAll(com.github.javaparser.ast.stmt.WhileStmt.class).size();
        complexity += method.findAll(com.github.javaparser.ast.stmt.ForStmt.class).size();
        complexity += method.findAll(com.github.javaparser.ast.stmt.SwitchStmt.class).size();
        complexity += method.findAll(com.github.javaparser.ast.stmt.TryStmt.class).size(); // Prendre en compte les blocs try-catch
        return complexity;
    }

    public Map<String, MethodInfo> getMethodComplexityMap() {
        return methodComplexityMap;
    }

    public static class MethodInfo {
        String className;
        String methodName;
        String parameterTypes; // Champ pour les types de paramètres
        int cyclomaticComplexity;

        public MethodInfo(String className, String methodName, String parameterTypes, int cyclomaticComplexity) {
            this.className = className;
            this.methodName = methodName;
            this.parameterTypes = parameterTypes;
            this.cyclomaticComplexity = cyclomaticComplexity;
        }

        @Override
        public String toString() {
            return className + "." + methodName + " (" + parameterTypes + "): CC = " + cyclomaticComplexity;
        }
    }
}
