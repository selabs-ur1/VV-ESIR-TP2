package fr.istic.vv;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;   // Import the FileWriter class

// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class NoGetterReporter extends VoidVisitorWithDefaults<Void> {

	 public List<String> Dec = null;
	 public List<String> Met = null;
	public FileWriter NoGetterWriter = null;
	String className;
	
    @Override
    public void visit(CompilationUnit unit, Void arg) {
    	//reseting the arrays
    	Dec = new ArrayList<String>();
    	Met = new ArrayList<String>();
    	
        //get the current package
        PackageDeclaration p = unit.findAll(PackageDeclaration.class).get(0);
        
        
    	try {
			NoGetterWriter = new FileWriter(System.getProperty("user.dir")+"\\NoGetterReport.txt",true);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	//iteration through all the typeDeclaration of the current class
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }

        
        //Comparing the two arrays of private field declaration and public methods
        try {
        	for(String privVar:Dec) {
            	boolean privateFieldHasMethod = false;
               	for(String method:Met) {
            		if(method.contains("get"+privVar)) {
            			//if one of the method contains "get<variable-name>" then we break 
            			//the loop and check for other private fields
            			privateFieldHasMethod=true;
            			break;
            		}
            	}
               	if(!privateFieldHasMethod) {
               		System.out.println("Warning in "+p.getNameAsString()+" in class "+className+": Private Variable '"+privVar+"' does not have a getter.\n");
        			NoGetterWriter.write("Warning in "+p.getNameAsString()+" in class "+className+": Private Variable '"+privVar+"' does not have a getter.\n");
               	}
        	}
        	
        	
	        NoGetterWriter.close();
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if(!declaration.isPublic()) return;
        //System.out.println(declaration.getFullyQualifiedName().orElse("[Anonymous]"));
        for(MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }
        // Printing nested types in the top level
        for(BodyDeclaration<?> member : declaration.getMembers()) {
                member.accept(this, arg);
        }
    }
    

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
    	className = declaration.getNameAsString();
        visitTypeDeclaration(declaration, arg);
    }
    
    @Override
    public void visit(FieldDeclaration declaration,Void arg) {
       	String tmp = declaration.getVariables().get(0).getName().toString();
    	Dec.add(tmp);
    }

    @Override
    public void visit(EnumDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        if(!declaration.isPublic()) return;
        Met.add(declaration.getDeclarationAsString(true, true).toLowerCase());
    }

}
