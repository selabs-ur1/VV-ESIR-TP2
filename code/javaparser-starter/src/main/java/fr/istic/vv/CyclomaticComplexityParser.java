package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

public class CyclomaticComplexityParser extends VoidVisitorWithDefaults<Void> {
    MethodInfoNode rootInfo = new MethodInfoNode();
    MethodInfoNode currentInfo = rootInfo;
    
    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }
    /**
     * Visit all classes, methods, parameters of a project.
     */
    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        
        /*System.out.println(declaration.getFullyQualifiedName().orElse("[Anonymous]"));
        String packageName = declaration.findCompilationUnit().get().getPackageDeclaration()
        .map(pkg -> pkg.getName().asString()).orElse("[None]");
        System.out.println(packageName);*/
        MethodInfoNode tmpInfo = new MethodInfoNode(declaration.getFullyQualifiedName().orElse("[Anonymous]"));
        currentInfo.addChild(tmpInfo);
        currentInfo = tmpInfo;
        for(MethodDeclaration method : declaration.getMethods()) {
            currentInfo.addChild(new MethodInfoNode(Integer.toString(computeComplexity(method))));
            method.accept(this, arg);
            for( Parameter param : method.getParameters()) {
                param.accept(this, arg);
            }
            currentInfo = tmpInfo;

        }
        for(BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration)
                member.accept(this, arg);
        }
        currentInfo = rootInfo;
    }

    /**
     * Calculates the complexity of the method passed as a parameter.
     * @param method method whose complexity is to be calculated
     * @return the cyclomatic complexity
     */
    private int computeComplexity(MethodDeclaration method) {
            int complexity = 1;

            complexity+=method.findAll(IfStmt.class).size();
            complexity+=method.findAll(WhileStmt.class).size();
            complexity+=method.findAll(ForStmt.class).size();
            complexity+=method.findAll(ConditionalExpr.class).size();
            complexity+=method.findAll(DoStmt.class).size();

            return complexity;
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
        MethodInfoNode tmpInfo = new MethodInfoNode(declaration.getDeclarationAsString(true, true));
        currentInfo.addChild(tmpInfo);
        currentInfo = tmpInfo;
    }
    
    @Override
    public void visit(Parameter declaration, Void arg) {
        MethodInfoNode tmpInfo = new MethodInfoNode(declaration.getTypeAsString());
        currentInfo.addChild(tmpInfo);
    }

    public MethodInfoNode getCurrentInfo() {
        return rootInfo;
    }

}
