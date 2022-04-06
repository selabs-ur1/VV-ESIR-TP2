package nogetter.nogetter;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods

public class PublicElementsPrinter2 extends VoidVisitorWithDefaults<Void> {
	
	private ArrayList<String> method_names = new ArrayList<>();
	private String output;
	
	public PublicElementsPrinter2() {
		output = "";
	}
	
	
	// ----
	public String getOutput() {
		return output;
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
		//System.out.println(declaration.getFullyQualifiedName().orElse("[Anonymous]"));
		output += declaration.getFullyQualifiedName().orElse("[Anonymous]") + "\n";
		
		for (MethodDeclaration method : declaration.getMethods()) {
			method.accept(this, arg);
		}
		
		// Printing nested types in the top level
		for (BodyDeclaration<?> member : declaration.getMembers()) {
			if (member instanceof TypeDeclaration)
				member.accept(this, arg);
		}
		
		for (FieldDeclaration field : declaration.getFields()) {
			visit(field, arg);
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
			method_names.add(declaration.getNameAsString() + ":" + declaration.getTypeAsString());
		}
	}
	
	@Override
	public void visit(FieldDeclaration declaration, Void arg) {
		if (declaration.isPublic())
			return;
		String name = getNameMethodOfAttribute(declaration.toString()) + ":" + declaration.getElementType();
		
		for(String s : method_names) {
			if(name.equals(s)) {
				output += "    " + declaration + "\n";
				//System.out.println("    " + declaration);
			}
		}
	}
	
	
	// ----
	private String getNameMethodOfAttribute(String name) {
		String retour = "";
		String[] string_array = name.split(" ");
		String tamp = string_array[string_array.length-1];
		retour = "get" + tamp.substring(0, 1).toUpperCase() + tamp.substring(1, tamp.length()-1);
		return retour;
	}

}
