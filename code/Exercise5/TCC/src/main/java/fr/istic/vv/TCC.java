package fr.istic.vv;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

public class TCC extends VoidVisitorWithDefaults<Void> {
	//private List<String> meth= new ArrayList<String>();
	
	private Set<String> classField;
	private Map<String, Set<String>> methodVar;
	private String file;
	
	public TCC(String file) {
		this.file=file;
	}
	
    @Override
    public void visit(CompilationUnit unit, Void arg) {
    	classField = new HashSet<String>();
    	methodVar = new HashMap<>();
    	
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
        
        calculTCC();
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if(!declaration.isPublic()) return;
        file = file + ("Package : "+declaration.getFullyQualifiedName().orElse("[Anonymous]") + " : \n");
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
		List<FieldDeclaration> fields = declaration.getFields();
		
		for (FieldDeclaration f : fields) {
        	NodeList<VariableDeclarator> var = f.getVariables();
            for(VariableDeclarator v : var) {
            	classField.add(v.getNameAsString());
            }
        }
        visitTypeDeclaration(declaration, arg);
        file = file + "\tClass : "+declaration.getNameAsString()+"\n";
    }

    @Override
    public void visit(EnumDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
    	
        Set<String> variables = new HashSet<String>();
        for (FieldAccessExpr f : declaration.findAll(FieldAccessExpr.class)) {
        	variables.add(f.getNameAsString());
        }
        for (NameExpr f : declaration.findAll(NameExpr.class)) {
        	variables.add(f.getNameAsString());
        }
        
        variables.retainAll(classField);
        methodVar.put(declaration.getNameAsString(), variables);
        
    }
    
    public String getFile() {
    	return file;
    }
    
    public void calculTCC () {
    	int denominateur = (methodVar.size() * (methodVar.size()-1))/2;
        
        int indice=0;
        int res=0;
        for (Set<String> value : methodVar.values()) {
        	int cpt=0;

        	for (Set<String> part : methodVar.values()) {
        		if(cpt>indice) {
	        		Set<String> result = new HashSet<>(value);
	        		
	        		result.retainAll(part);
	        		if(!result.isEmpty()) {
	        			res++;
	        		}
        		}
        		cpt++;
        	}
        	indice++;
        }
        file = file +("\t\t- TCC : "+res+"/"+denominateur+"\n");   
    }
}
