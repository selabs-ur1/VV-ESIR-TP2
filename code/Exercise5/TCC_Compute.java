package fr.istic.vv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

// This class visits a compilation unit and
// store in a static attribute you can access with getResult(), all the attributes which have no getter
public class TCC_Compute extends VoidVisitorWithDefaults<Void> {

	//list qui contiendra tous les attributs d'une classe
	private static ArrayList<String> tmp_attribute_in_class;
	//dictionnaire qui contiendra toutes les méthodes(valeurs) utilisant un attribut(clé)
	//; et ce pour une classe donnée
	private static HashMap<String,ArrayList<String>> tmp_classe_attributs_et_methodes;
	private static HashMap<String, Double> map_methodes_TCC = new HashMap<String , Double>();
	private static String result="";
	
	public static String getResult()
	{
		return TCC_Compute.result;
	}
	
	public static HashMap<String, Double> getMapTCC()
	{
		return map_methodes_TCC;
	}
	
    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }
    
    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if(!declaration.isPublic()) return; // si la classe n'est pas publique on sort
        
        // Sinon:
        // on parcourt les attributs de la classe et on les stocke
        tmp_attribute_in_class = new ArrayList<String>();
        for(FieldDeclaration field : declaration.getFields())
        {
        	field.accept(this, arg);
        }
        
        // Construction du dictionnaire associant les attributs 
        // et les méthodes dans lesquelles on les utilise
        // EX: {attr1=[method1, method3,..], attr2=[method4, method6],...}
        tmp_classe_attributs_et_methodes = new HashMap<String, ArrayList<String>>();
        for(MethodDeclaration method : declaration.getMethods()) 
        {
        	method.accept(this, arg);
        }
        
        // Construction du dictionnaire des liens entre les méthodes
        // MAIS on ne va pas dupliquer les liens
        // EX: 
        //     Pour {attr1=[method1, method2], attr2=[method1, method2, method3]}
        //     on obtient :{method1=[method2, method3], method2=[method3], method3=[]}
        HashMap<String, ArrayList<String>> dico_liens = new HashMap<String, ArrayList<String>>();
        tmp_classe_attributs_et_methodes.forEach( (key, value) -> {
        	for(String methode : value)
        	{
	        	if(!dico_liens.containsKey(methode))
	        	{
	        		ArrayList<String> new_list = new ArrayList<String>();
	        		dico_liens.put(methode, new_list);
	        	}
	        	for(String methode_liee : value)
	        	{
	        		//si la methode_liee n'est pas la méthode clé, et qu'elle n'est pas déjà dans la liste, 
	        		//et que la clé n'est pas déjà en tant que valeur dans la liste de la methode_liee
	        		boolean methode_liee_diff2_method = !methode_liee.contentEquals(methode);
	        		boolean methode_liee_absente_list2_methode = !dico_liens.get(methode).contains(methode_liee);
	        		
	        		boolean acces_valide = dico_liens.containsKey(methode_liee);
	        		boolean methode_absente_list2_methode_liee = ( acces_valide && !dico_liens.get(methode_liee).contains(methode) ) || (!acces_valide);
	        		if( methode_liee_diff2_method &&  methode_liee_absente_list2_methode && methode_absente_list2_methode_liee )
	        		{
	        			dico_liens.get(methode).add(methode_liee);
	        		}
	        	}
        	}
        });
        
        // Calcul du TCC à partir du dictionnaire juste construit
        double NDC = 0;//nombre de connections de la classe
        for (Map.Entry<String, ArrayList<String>> method_row : dico_liens.entrySet())
        {
        	NDC += method_row.getValue().size();
        }
        double TCC = Math.round(NDC/coeff_binomial(dico_liens.size())*1000)/1000.0;
        
        // Ecriture du contenu du fichier DOT pour la visualisation des graphes de cohésion
        // EX :
		//        strict graph { 
		//        a -- b
		//        a -- b
		//        b -- a [color=blue]
		//      } 
        String graph ="strict graph { ";
        for (Map.Entry<String, ArrayList<String>> method_row : dico_liens.entrySet())
        {
        	graph += method_row.getKey()+" -- { ";
        	for (String method_liee : method_row.getValue())
    		{
    			graph += method_liee+" ";
    		}
        	graph += "}  ";
        }
        graph+="}";
        
        String dot_file_name =declaration.getName()+".dot";
        try {
        	Path path = Paths.get("dotFiles");
        	if(!Files.exists(path))
        	{
        		Files.createDirectory(path);
        	}
            FileWriter fw = new FileWriter(new File("dotFiles", dot_file_name));
            fw.write(graph);
            fw.close();
          } catch (IOException e) {
            e.printStackTrace();
          } 
        
        // Ajout de la valeur du TCC calculé à la map_methodes_TCC pour tracer 
        // l'histogramme des TCC pour les différentes classes
        TCC_Compute.map_methodes_TCC.put(declaration.getNameAsString(), TCC);
        
        // Concaténation de la chaine result pour le fichier contenant pour chaque classe
        // le nom du package et le TCC de la classe
        result += "classe : " + declaration.getNameAsString() + "  ,  package : " + declaration.getClass().getPackageName() +
        "  ,  TCC = " + TCC +"\n";

        
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
        
        String body = declaration.getBody().toString();
        for( String var : TCC_Compute.tmp_attribute_in_class)
        {
        	//si la méthode utilise un attribut de la classe, on l'ajoute au dictionnaire
        	if(body.contains(var))
        	{
        		if(TCC_Compute.tmp_classe_attributs_et_methodes.containsKey(var))
        		{
        			TCC_Compute.tmp_classe_attributs_et_methodes.get(var).add(declaration.getName().toString());
        		}
        		else
        		{
        			ArrayList<String> new_list = new ArrayList<String>();
        			new_list.add(declaration.getName().toString());
        			TCC_Compute.tmp_classe_attributs_et_methodes.put(var, new_list);
        		}
        	}
        }
    }
    
    public void visit(FieldDeclaration field, Void arg)
    {
    	//on ajoute l'attribut à la liste des attributs de la classe
    	for (  VariableDeclarator  var : field.getVariables())
    	{
    		TCC_Compute.tmp_attribute_in_class.add(var.getName().toString());
    	}
    }

    // on tolère le cas 2 > n ici en évitant de renvoyer un zéro (qui provoquerait une division par zéro)
    // ce choix est tout à fait acceptable pour notre utilisation car le numérateur du calcul du TCC
    // sera toujours nul dans le cas où 2 > n
    private int coeff_binomial(int n)
    {
    	
    	if(2>n)
    	{
    		return 1;
    	}
    	return n*(n-1)/2;
    }
    
    // Remarque : ici on n'a pas implémenté de manière générale le coefficient binomiale
    // Car on peut formaliser plus simple pour ce cas, car k est toujours égal à 2
    // étant donné que l'on regarde le nombre de paires possibles dans un ensemble
    // Ainsi n!/(2(n-2)!) = n*(n-1)!*(n-2)!/(2(n-2)!) = n(n-1)/2
    //
    //Ce choix de simplification est d'autant plus intéressant que l'on aurait du manipuler
    // des grands nombres avec les factorielles pouvant être source d'erreurs
    
}
