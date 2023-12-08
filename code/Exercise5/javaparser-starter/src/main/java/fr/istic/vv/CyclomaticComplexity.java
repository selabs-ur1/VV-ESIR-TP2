package fr.istic.vv;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

public class CyclomaticComplexity extends VoidVisitorWithDefaults<Void> {
  public static class MethodInfo {
    private String methodName;
    private String className;
    private String packageName;
    private int cyclomaticComplexity;
    private List<Type> parametersTypes;

    public MethodInfo(String methodName, String className, String packageName,
        List<Type> parameterTypes, int cyclomaticComplexity) {
      this.methodName = methodName;
      this.className = className;
      this.packageName = packageName;
      this.parametersTypes = parameterTypes;
      this.cyclomaticComplexity = cyclomaticComplexity;
    }

    public String getMethodName() {
      return methodName;
    }

    public String getClassName() {
      return className;
    }

    public String getPackageName() {
      return packageName;
    }

    public int getCyclomaticComplexity() {
      return cyclomaticComplexity;
    }

    public List<String> getParametersTypes() {
      ArrayList<String> parametersTypesNames = new ArrayList<>();
      for (Type type : parametersTypes) {
        parametersTypesNames.add(type.toString());
      }
      return parametersTypesNames;
    }

  }

  private Set<MethodInfo> methods;

  private Map<Integer, Integer> distribution;

  public CyclomaticComplexity() {
    this.methods = new HashSet<>();
    this.distribution = new HashMap<>();
  }

  public Set<MethodInfo> getMethodsInfo() {
    return methods;
  }

  @Override
  public void visit(CompilationUnit unit, Void arg) {
    for (TypeDeclaration<?> type : unit.getTypes()) {
      type.accept(this, null);
    }
  }

  public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
    if (!declaration.isPublic())
      return;
    // System.out.println(declaration.getFullyQualifiedName().orElse("[Anonymous]"));
    for (MethodDeclaration method : declaration.getMethods()) {
      method.accept(this, arg);
    }
    // Printing nested types in the top level
    for (BodyDeclaration<?> member : declaration.getMembers()) {
      if (member instanceof TypeDeclaration)
        member.accept(this, arg);
    }
  }

  @Override
  public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
    String className = declaration.getNameAsString();
    String packageName = declaration.findCompilationUnit().get().getPackageDeclaration()
        .map(pkg -> pkg.getName().asString()).orElse("[Default Package]");
    for (BodyDeclaration<?> member : declaration.getMembers()) {
      if (member instanceof MethodDeclaration) {
        MethodDeclaration method = (MethodDeclaration) member;

        String methodName = method.getNameAsString();
        ArrayList<Type> parameters = new ArrayList<>();
        for (Parameter param : method.getParameters()) {
          parameters.add(param.getType());
        }
        int cyclomaticComplexity = analyzeMethod(method);
        MethodInfo m = new MethodInfo(methodName, className, packageName, parameters, cyclomaticComplexity);
        methods.add(m);
        distribution.put(cyclomaticComplexity, distribution.getOrDefault(cyclomaticComplexity, 0) + 1);
      }
    }
    visitTypeDeclaration(declaration, arg);
  }

  private int analyzeMethod(MethodDeclaration method) {
    final int[] methodComplexity = { 1 };

    method.findAll(IfStmt.class).forEach(stmt -> methodComplexity[0]++);
    method.findAll(WhileStmt.class).forEach(stmt -> methodComplexity[0]++);
    method.findAll(ForStmt.class).forEach(stmt -> methodComplexity[0]++);
    method.findAll(ConditionalExpr.class).forEach(stmt -> methodComplexity[0]++);
    method.findAll(DoStmt.class).forEach(stmt -> methodComplexity[0]++);

    return methodComplexity[0];
  }

  @Override
  public void visit(EnumDeclaration declaration, Void arg) {
    visitTypeDeclaration(declaration, arg);
  }

  @Override
  public void visit(MethodDeclaration declaration, Void arg) {
    if (!declaration.isPublic())
      return;
  }

  public Map<Integer, Integer> getDistribution() {
    return distribution;
  }
}
