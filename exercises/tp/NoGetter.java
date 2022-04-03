package tp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.SimpleName;

import fr.istic.vv.PublicElementsPrinter;

public class NoGetter extends PublicElementsPrinter {
	
	private ArrayList<String> varWithoutGetter = new ArrayList<String>(); //Will hold every private attribute ; those with a getter will be removed from the List.
	private ArrayList<String> varGetter = new ArrayList<String>(); //Will hold every attribute that has a getter
	
	@Override
	public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if(!declaration.isPublic()) return;
        
        for(MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }
        
        //To visit the attributes of the class
        for(FieldDeclaration field : declaration.getFields()) {
        	field.accept(this, arg);
        }
        
        //We remove every attribute from varWithoutGetter that does have a getter
        for(String varDoesHaveGetter: varGetter) {
        	for(String varNoGetter: varWithoutGetter) {
	        	if(varDoesHaveGetter.equals(varNoGetter.toLowerCase())) {
	        		varWithoutGetter.remove(varNoGetter);
	        		break;
	        	}
        	}
        }
        
        //We build the final .txt
        try {
            File myObj = new File(declaration.getNameAsString()+".txt");
            if (myObj.createNewFile()) {
              System.out.println("File created: " + myObj.getName());
            } else {
              System.out.println("File already exists.");
            }
            FileWriter myWriter = new FileWriter(declaration.getNameAsString()+".txt");
            myWriter.write(declaration.getFullyQualifiedName().orElse("[Anonymous]")+"\n");
            if(varWithoutGetter.size()==0) {
            	myWriter.write("   Every private attribute does have a getter.\n");
            }
            else {
	            for(String var: varWithoutGetter) {
	            	myWriter.write("   "+var+"\n");
	            }
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
            
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }
	
	//visit the attributes
	@Override
    public void visit(final FieldDeclaration n, final Void arg) {
		if(!n.isPublic()) {//If the field is private
			NodeList<VariableDeclarator> truc = n.getVariables();
			for(VariableDeclarator varDcl : truc) {
				varDcl.accept(this, null);
			}
		}
    }
	
	//visit a single variable
	@Override
    public void visit(final VariableDeclarator n, final Void arg) {
		varWithoutGetter.add(n.getNameAsString());
    }
	
	//we look for getters
    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        if(!declaration.isPublic()) return;
        String name = declaration.getNameAsString();
        if(name.length()>3 && name.subSequence(0, 3).equals("get")){//If it is a getter
        	varGetter.add(name.subSequence(3, name.length()).toString().toLowerCase());
        }
        System.out.println("  " + declaration.getDeclarationAsString(true, true));
    }

}
