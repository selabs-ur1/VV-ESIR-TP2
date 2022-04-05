package fr.istic.vv;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {

	private List<String> meth= new ArrayList<String>();
	
    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if(!declaration.isPublic()) return;
        System.out.println(declaration.getFullyQualifiedName().orElse("[Anonymous]"));
        for(MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }
        // Printing nested types in the top level
        for(BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration) {
                member.accept(this, arg);
            }
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
    	String file = "";
        visitTypeDeclaration(declaration, arg);
        List<FieldDeclaration> fields = declaration.getFields();
        for (FieldDeclaration f : fields) {
        	if(!f.isPublic()) {
        		NodeList<VariableDeclarator> var = f.getVariables();
                for(VariableDeclarator v : var) {
                	for(String m : meth) {
                		String var_name = v.getNameAsString();
                		String var_upper = var_name.substring(0, 1).toUpperCase() + var_name.substring(1);
	                	if(m.contains(var_upper)) {
	                		file = file+"Method "+m+" for : "+v.getNameAsString()+"\n";
	                	}
	                	else {
	                		file = file+"No getter for " +v.getNameAsString()+"\n";
	                	}
                	}
            	}
        	}
        }
        try (PrintWriter out = new PrintWriter("ex4.txt")) {
            out.println(file);
        } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public void visit(EnumDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
    	
        if(!declaration.isPublic()) return;
        
    	if(declaration.getDeclarationAsString(true, true).contains("get")) {
    		meth.add(declaration.getDeclarationAsString(true, true));
    	}
 
    	
        
    }

}
