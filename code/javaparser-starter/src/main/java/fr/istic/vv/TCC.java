package fr.istic.vv;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import org.apache.commons.io.FileUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class TCC extends VoidVisitorWithDefaults<Void> {

	
    @Override
    public void visit(CompilationUnit unit, Void arg) {
    	
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
        System.out.println("test");
    }

    //Lecture récusive de code pour analyse de body ou d'autres expressions. 
    public void recursExpr(Node s, ArrayList<String> arrNames, ArrayList<String> arrMethod) {
    	
    	String[] classNameLong = s.getClass().toString().split("[.]");
		String className = classNameLong[(classNameLong.length)-1].toLowerCase();
		
		// Si c'est un nom d'expression ou un simpleName
    	if(className.equals("nameexpr") || className.equals("simplename")) {
    		arrNames.add(s.toString());
    		return;
    	}
    	
    	// Si c'est une méthode
    	if(className.equals("methodcallexpr")) {
    		if(arrMethod != null) {
	    		String AppelMethod =  s.toString().split("[(]")[0];
	    		String[] AppelMethodArr = AppelMethod.split("[.]");
	    		String nomMethod = AppelMethodArr[AppelMethodArr.length-1];
	    		arrMethod.add(nomMethod);
    		}
    	}
    	
    	// Pour chaque enfants du noeud
    	for(Node n: s.getChildNodes()) {
			recursExpr(n,arrNames,arrMethod);
		}
    	
    }
    
    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
    	// Si la classe est publique
        if(!declaration.isPublic()) return;
        String content = "";
        
        content += "\n*********************  "+declaration.getNameAsString()+"  *****************\n\n";
        
        // Tableau des champs 
        ArrayList<String> fields = new ArrayList<>();
        // Tableau des métodes
        ArrayList<String> methods = new ArrayList<>();
        // map des liaisons méthodes(key) - attribut(values)
        Map <String,ArrayList<String>> hashMethodAttrs = new HashMap<>(); 
        // map des liaisons méthodes(key) - méthodes(values)
        Map <String,ArrayList<String>> hashMethodMethod = new HashMap<>(); 
        
        //Pour chaque variable de la classe
        for(FieldDeclaration field : declaration.getFields()) {
        	field.accept(this, arg);
        	//On les sauvegarde dans fields
        	recursExpr(field,fields,null);
        }
        
        //Pour chaque methodes de la classe
        for(MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
            //on les sauvegardes dans methods
            methods.add(method.getNameAsString());
        }
        
        //Pour chaque méthodes de la classe --> Optimisable donc, mais plus simple à la compréhension
        for(MethodDeclaration method : declaration.getMethods()) {
        	//on va récupérer les attributs et les méthods utilisé dans le body de la méthode
    		ArrayList<String> tmpFields = new ArrayList<>();
            ArrayList<String> tmpMethods = new ArrayList<>();
            //s'il y a un body
            if(method.getBody().isPresent()) {
            	//pour chaque parties du body
	        	for (Statement n : method.getBody().get().getStatements()) {	                
	        		recursExpr(n,tmpFields,tmpMethods);
	        	}
	        	
	        	//Vérification si ce sont des attributs ou des méthodes de la classe.
	        	Iterator<String> iter = tmpFields.iterator();
	        	while(iter.hasNext()) {
	        		String possiblefield = iter.next();
	        		if(!fields.contains(possiblefield)) {
	        			iter.remove();
	        		}
	        	}
	        	Iterator<String> iter2 = tmpMethods.iterator();
	        	while(iter2.hasNext()) {
	        		String possiblemethod = iter2.next();
	        		if(!methods.contains(possiblemethod)) {
	        			iter2.remove();
	        		}
	        	}
            }
    		
	        	
            //On ajoute cette méthode aux map
    		hashMethodAttrs.put(method.getNameAsString().toString(),tmpFields);
    		hashMethodMethod.put(method.getNameAsString().toString(),tmpMethods);
        }
        content+="Variable trouvé : ";
        content += hashMethodAttrs.toString() +"\n";    
        content+="Méthode trouvé : " ;    
        content+=hashMethodMethod +"\n";
        

		// détection des liasions attribut entre méthodes
		Map <String, Set<String>> liaisons = new HashMap<>(); 
		for (String key : hashMethodAttrs.keySet()) {
			liaisons.put(key,new HashSet<>());
			for(String attr : hashMethodAttrs.get(key)) {
				for(String key2 : hashMethodAttrs.keySet()) {
					if( !key.equals(key2) && !liaisons.get(key).contains(key2)) {
						if(!(liaisons.containsKey(key2) && liaisons.get(key2).contains(key)))
							if(hashMethodAttrs.get(key2).contains(attr)) {
								liaisons.get(key).add(key2);
							}
					}
				}
			}
		}
		
		//J'avais pris les méthodes pour calculer le LCC mais ce n'était pas à faire
		
		//TODO détection liaisons méthodes
		
		
		content+="attribut de liaison trouvé : " ; 
		content+=liaisons+"\n";
		
		
		//Calcul TCC + dotCode
		Set<String> tmpmethods = new HashSet<>();
		float res =0;
		String dotFormat="";
		for (String key : liaisons.keySet()) {
			for(String key2 : liaisons.get(key) ) {
				tmpmethods.add(key);
				tmpmethods.add(key2);
				dotFormat+=key +" -- "+key2+";";
				res += 1;
			}
		}
		
		for(String method : methods) {
			if(!tmpmethods.contains(method)) {
				dotFormat+=method+";";
			}
		}
		
		int nbKey = liaisons.keySet().size();
		int denominateur = (nbKey*(nbKey-1))/2;
		
		if(denominateur>0) {
			res/=denominateur;
		}
		
			
		content +="TCC de : " + res;
		
		//creation .do, commande unix pour le générer : dot -Tsvg input.dot -o output.svg
        createDotGraph(dotFormat, declaration.getNameAsString());
        
        //création rapport
        String path = "./reportTcc.txt";
		
        try {
            Files.write(Paths.get(path), content.getBytes(),StandardOpenOption.CREATE,StandardOpenOption.APPEND);

        } catch (IOException e) {
            e.printStackTrace();
        }

 
    }

    //Pas réussi à avoir les PDF
	public static void createDotGraph(String dotFormat,String className)
	{
		GraphViz gv=new GraphViz();
		gv.addln(gv.start_graph());
		gv.add(dotFormat);
		gv.addln(gv.end_graph());
		String type = "pdf";
		gv.decreaseDpi();
		gv.decreaseDpi();
		gv.getGraph( gv.getDotSource(), type, className );
		/*String type = "pdf";
		File out = new File(fileName+"."+ type); 
		gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), type ), out );*/
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
