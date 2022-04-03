package fr.istic.vv;

import java.util.ArrayList;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

public class NoGetter extends VoidVisitorWithDefaults<Void> {
	
    @Override
    public void visit(CompilationUnit unit, Void arg) {
    	
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if(!declaration.isPublic()) return;
        

    	ArrayList<String> fieldsPrivate = new ArrayList<>();
        
        for(FieldDeclaration field : declaration.getFields()) {
        	field.accept(this, arg);
        	if(!field.isPublic()) {
        		//System.out.println("  ->" + field.getVariable(0).getName());
        		fieldsPrivate.add(field.getVariable(0).getName().toString());
        	}
        }
    	
        for(MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
            if(method.getNameAsString().toString().substring(0,3).equals("get")) {
	            String nameMethod = method.getNameAsString().replace("get","");
	            if(fieldsPrivate.contains(nameMethod.toLowerCase())) {
	            	//TODO améliorer pour scan le code de la méthode privé
	            	fieldsPrivate.remove(nameMethod.toLowerCase());
	            }
            }
        }
        if(fieldsPrivate.size()>0) {
	        System.out.println("Ces attributs privé dans la classe "+ declaration.getNameAsString()  +" n'ont pas de getters: ");
	        for(String privateVar : fieldsPrivate) {
	        	System.out.println("  ->" + privateVar);
	        }
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
}
