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
        String className = method.findAncestor(ClassOrInterfaceDeclaration.class).map(ClassOrInterfaceDeclaration::getNameAsString).orElse("Unknown");
        
        int cyclomaticComplexity = calculateCyclomaticComplexity(method);
        
        methodComplexityMap.put(methodName, new MethodInfo(className, methodName, cyclomaticComplexity));
    }

    private int calculateCyclomaticComplexity(MethodDeclaration method) {
        // Simple implementation to count control flow statements
        int complexity = 1; // Start with 1 for the method itself
        complexity += method.findAll(com.github.javaparser.ast.expr.BinaryExpr.class).size();
        complexity += method.findAll(com.github.javaparser.ast.stmt.IfStmt.class).size();
        complexity += method.findAll(com.github.javaparser.ast.stmt.WhileStmt.class).size();
        complexity += method.findAll(com.github.javaparser.ast.stmt.ForStmt.class).size();
        complexity += method.findAll(com.github.javaparser.ast.stmt.SwitchStmt.class).size();
        complexity += method.findAll(com.github.javaparser.ast.stmt.TryStmt.class).size(); // Handle try-catch blocks if needed
        return complexity;
    }

    public Map<String, MethodInfo> getMethodComplexityMap() {
        return methodComplexityMap;
    }

    public static class MethodInfo {
        String className;
        String methodName;
        int cyclomaticComplexity;

        public MethodInfo(String className, String methodName, int cyclomaticComplexity) {
            this.className = className;
            this.methodName = methodName;
            this.cyclomaticComplexity = cyclomaticComplexity;
        }

        @Override
        public String toString() {
            return className + "." + methodName + ": CC = " + cyclomaticComplexity;
        }
    }
}
