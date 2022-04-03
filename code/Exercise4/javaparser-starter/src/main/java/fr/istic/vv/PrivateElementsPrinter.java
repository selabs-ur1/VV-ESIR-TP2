package fr.istic.vv;

import java.util.HashMap;
import java.util.Map;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

public class PrivateElementsPrinter extends VoidVisitorWithDefaults<Void> {
	
	private Map<String, MethodDeclaration> privateVarsWithGetter;
	
	public PrivateElementsPrinter() {
		privateVarsWithGetter = new HashMap<>();
	}
	
	@Override
	public void visit(CompilationUnit unit, Void arg) {
		for (TypeDeclaration<?> type : unit.getTypes()) {
			type.accept(this, null);
		}
	}

	public void visitClassDeclaration(TypeDeclaration<?> declaration, Void arg) {

		if (!declaration.isPublic())
			return;

		// Printing nested types in the top level
		for (BodyDeclaration<?> member : declaration.getMembers()) {
			if (member instanceof FieldDeclaration) //TypeDeclaration
				member.accept(this, arg);
		}
		
		//System.out.println(declaration.getFullyQualifiedName().orElse("[Anonymous]"));
		for (MethodDeclaration method : declaration.getMethods()) {
			method.accept(this, arg);
		}
		
		for(String var : privateVarsWithGetter.keySet()) {
			if(privateVarsWithGetter.get(var) == null) {
				System.out.println(var);
			}
		}
	}


	@Override
	public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
		visitClassDeclaration(declaration, arg);
	}

	@Override
	public void visit(EnumDeclaration declaration, Void arg) {
		
	}
	
 	@Override
	public void visit(FieldDeclaration declaration, Void arg) {
 		//System.out.println(declaration.toString() + " " + declaration.getAccessSpecifier());
 		if(isVariablePrivate(declaration)) {
 			String key = declaration.toString().split(" ")[2];
 			key = key.substring(0, key.length()-1);
 			privateVarsWithGetter.put(key, null);
 		}
 	}

	private boolean isVariablePrivate(FieldDeclaration declaration) {
		return declaration.getAccessSpecifier().compareTo(AccessSpecifier.PRIVATE) == 0;
	}
	
	@Override
	public void visit(MethodDeclaration declaration, Void arg) {
		if (declaration.isPublic()) {
			String getterFor = declaration.getName().toString();
			
			if(getterFor.length() >= 4) {
				String hasGet = getterFor.substring(0, 3);
				String varName = getterFor.substring(3);
				
				varName = varName.substring(0,1).toLowerCase()+varName.substring(1);
				
				if(hasGet.compareTo("get")==0) { 
					if(privateVarsWithGetter.containsKey(varName)) {
						privateVarsWithGetter.put(varName, declaration);
					}
				}
			}
			
			
		}
			
		//System.out.println("  " + declaration.getDeclarationAsString(true, true));
	}
}
