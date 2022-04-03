package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



public class PublicMethodsPrinter extends VoidVisitorWithDefaults<Void> {
	
	private ArrayList<String> noms_des_methodes = new ArrayList<>();
	private String name;
	
	public PublicMethodsPrinter() {
		name= "";
	}
	
	public String getOutput() {
		return name;
	}

	@Override
	public void visit(CompilationUnit unit, Void arg) {
		for (TypeDeclaration<?> type : unit.getTypes()) {
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

	@Override
	public void visit(MethodDeclaration declaration, Void arg) {
		if (!declaration.isPublic())
			return;
		
		if(declaration.getNameAsString().startsWith("get")) {
			noms_des_methodes.add(declaration.getNameAsString() + ":" + declaration.getTypeAsString());
		}
	}
	
	@Override
	public void visit(FieldDeclaration declaration, Void arg) {
		if (declaration.isPublic())
			return;
		String name = getMethodOfAttribute(declaration.toString()) + ":" + declaration.getElementType();
		
		for(String s : noms_des_methodes) {
			if(name.equals(s)) {
				name += " " + declaration + " ;";
				
			}
		}
	}
	public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
		if (!declaration.isPublic())
			return;
		
		name += declaration.getFullyQualifiedName() + "\n";
		
		for (MethodDeclaration method : declaration.getMethods()) {
			method.accept(this, arg);
		}
		

		for (BodyDeclaration<?> member : declaration.getMembers()) {
			if (member instanceof TypeDeclaration)
				member.accept(this, arg);
		}
		
		for (FieldDeclaration field : declaration.getFields()) {
			visit(field, arg);
		}
	}

	private String getMethodOfAttribute(String element) {
		String valeur = "";
		String[] element_array = element.split(" ");
		String a = element_array[element_array.length-1];
		valeur = "get" + a.substring(0, 1).toUpperCase() + a.substring(1, a.length()-1);
		return valeur;
	}
	public static void main(String[] args) {
		//test manuel
		//on crée un objet et l'observe les résultats sur le terminal avec la méthode getOutput
		PublicMethodsPrinter p=new PublicMethodsPrinter();
		p.getOutput();
	}
}

