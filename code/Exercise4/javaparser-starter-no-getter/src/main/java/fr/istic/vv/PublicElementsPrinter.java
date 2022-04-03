package fr.istic.vv;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {
	
	private List<String> listOfGetters = new ArrayList<String>();
	private String parseResultText="";
	
	/**
	 * Return the result of the parser
	 * @return the result
	 */
	public String getParseResulText() {
		return parseResultText;
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
		visitTypeDeclaration(declaration, arg);
		List<FieldDeclaration> listFields = declaration.getFields();
		ListIterator<FieldDeclaration> it = listFields.listIterator();
		while (it.hasNext()) {
			FieldDeclaration current = it.next();
			boolean isPrivate = current.toString().contains("private");
			if (isPrivate) {
				if(!hasGetter(current.getVariable(0).getNameAsString())) {
					parseResultText=parseResultText+"In package : "+declaration.getFullyQualifiedName().orElse("[Anonymous]")+"\n";
					parseResultText=parseResultText+"  In class or interface : "+declaration.getNameAsString()+"\n";
					parseResultText=parseResultText+"    The field "+current.getVariable(0).getNameAsString()+" has no getter\n\n";
				}
			}
		}
	}

	@Override
	public void visit(EnumDeclaration declaration, Void arg) {
		visitTypeDeclaration(declaration, arg);
	}

	@Override
	public void visit(MethodDeclaration declaration, Void arg) {
		if (!declaration.isPublic())
			return;
		boolean isGetter = declaration.getDeclarationAsString(true, true).contains("get");
		if (isGetter) {
			listOfGetters.add(declaration.getDeclarationAsString(true, true));
		}
	}
	
	/**
	 * Return if the field has a getter
	 * @param field : String of the field name
	 */
	public boolean hasGetter(String field) {
		ListIterator<String> it = listOfGetters.listIterator();
		boolean hasGetter=false;
		
		while(it.hasNext()) {
			String currentGetter = it.next();
			hasGetter = currentGetter.toLowerCase().contains(field);
			if(hasGetter) {
				return hasGetter;
			}
		}
		return hasGetter;
	}

}
