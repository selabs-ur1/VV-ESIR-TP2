package fr.istic.vv;

import java.util.ArrayList;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

// This class visits a compilation unit and
// store in a static attribute you can access with getResult(), all the attributes which have no getter
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {

	private static ArrayList<String> tmp_methods_in_class;
	private static String result="";
	
	public static String getResult()
	{
		return PublicElementsPrinter.result;
	}
	
    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if(!declaration.isPublic()) return; // si la classe n'est pas publique on sort
        
        //sinon:
        //on parcourt les méthodes et on stocke les méthodes qui sont des getters dans cette classe
        PublicElementsPrinter.tmp_methods_in_class = new ArrayList<String>();
        for(MethodDeclaration method : declaration.getMethods()) 
        {
        	method.accept(this, arg);
        }
        
        //visite des attributs --> voir visit(FieldDeclaration field, Void arg)
        for(FieldDeclaration field : declaration.getFields())
        {
        	field.accept(this, arg);
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
    	//si  la méthode n'est pas publique on sort
        if(!declaration.isPublic()) return;
        
      //sinon on l'ajoute à la liste des méthodes publiques
        String name = declaration.getName().toString();
        if(name.length()>3 && name.substring(0, 3).contentEquals("get"))
        {
        	PublicElementsPrinter.tmp_methods_in_class.add(name);
        }
    }
    
    public void visit(FieldDeclaration field, Void arg)
    {
    	// si l'attribut est publique on sort
    	if(field.isPublic())return; 
    	
    	// sinon on parcourt les différents variables de la déclaration, et on regarde si on trouve une méthode associée
    	for (  VariableDeclarator  var : field.getVariables())
    	{
    		//on met la première lettre de l'attribut en majuscule
    		String name = var.getName().toString();
    		String nameMaj = name.substring(0, 1).toUpperCase()+name.substring(1);
    		for(String method : PublicElementsPrinter.tmp_methods_in_class)
    		{
    			//tant qu'on ne trouve pas une méthode correspondant à l'attribut on continue
    			if(method.contentEquals("get"+nameMaj) || method.contentEquals("get"+name))
    			{
    				return;
    			}
    		}
    		//si on a pas trouvé:
    		PublicElementsPrinter.result+="attribut: "+name+" ,class: "+field.getClass().getSimpleName()+" ,package: "+field.getClass().getPackageName()+"\n";
    	}
    	
    }

}
