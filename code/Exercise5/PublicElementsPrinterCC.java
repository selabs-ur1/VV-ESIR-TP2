//Not finished

package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PublicElementsPrinterCC extends VoidVisitorWithDefaults<Void> {



    public int indexClass=-1;
    public int indexPackage =-1;

    private String fileName="";

    public final List<String> tabPackages; //liste des noms de package
    public final List<List<HashSet<String>>> tabParameters; //liste des noms d'attributs (package/class/attributs)
    public final List<List<HashSet<String>>> tabMethods; //liste des noms de getters (package/class/getters)
    public final List<List<HashSet<Integer>>> tabCC;
    public final List<List<String>> tabClassName;   //liste des classNames (package/class)

    public PublicElementsPrinterCC(){
        tabParameters= new ArrayList<>(); //Init la liste (package)
        tabMethods= new ArrayList<>();  //Init la liste (package)
        tabClassName=new ArrayList<>(); //Init la liste (package)
        tabPackages=new ArrayList<>(); //Init la liste de package
        tabCC = new ArrayList<>();
    }
    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    /**
     * Visiteur pour les méthodes
     * @param declaration
     * @param arg
     */
    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        for(MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }
        // Printing nested types in the top level
        for(BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration)
                member.accept(this, arg);
        }
    }

    /**
     * Visiteur pour les classes ou interfaces
     * @param declaration
     * @param arg
     */
    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        String packageOfDeclaration = getTabPackages(declaration.getFullyQualifiedName().toString());   //Get the tabPackages of the declaration
        if (tabPackages.isEmpty() || !(tabPackages.get(indexPackage).equals(packageOfDeclaration+" : "+fileName))) {    //if the set of tabPackages is empty (at start), add the tabPackages to the set at index 0.
            indexPackage++;
            tabPackages.add(indexPackage,packageOfDeclaration+" : "+fileName);    //Add the new package to the package list
            tabClassName.add(indexPackage,new ArrayList<>());  //Init a new list at index (indexPackage) (package/class) to get classes of package
            tabMethods.add(indexPackage, new ArrayList<>()); //Init a new list at index (indexPackage) (package/class/getters) to add all getters from package
            tabParameters.add(indexPackage, new ArrayList<>()); //Init a new list at index (indexPackage) (package/class/getters) to add all attributes from package
            tabCC.add(indexPackage, new ArrayList<>());
            indexClass=-1;
        }
        //WE HAVE TO CHECK THE NAME OF THE CLASS (IF IT IS DIFFERENT OR NOT)
        String classOfDeclaration = getNameOfClass(declaration.toString());

        if (tabClassName.get(indexPackage).isEmpty()){                                              //If the classlist is empty (at start of new package)
            tabClassName.add(indexPackage,new ArrayList<>());                                       //create a new arrayList
        }
        indexClass++;
        tabCC.get(indexPackage).add(indexClass, new HashSet<>());
        tabClassName.get(indexPackage).add(indexClass, classOfDeclaration);                         //add the class of Declaration to the set
        tabParameters.get(indexPackage).add(indexClass, new HashSet<>());                           //
        tabMethods.get(indexPackage).add(indexClass, new HashSet<>());                              //
        tabClassName.get(indexPackage).add(getNameOfClass(declaration.toString()));                 //Add the name of class of Declaration to the array of classNames.

        visitTypeDeclaration(declaration, arg);                                                     //
    }

    public String getTabPackages(String declaration){
        return declaration.substring(declaration.indexOf("[")+1,declaration.indexOf("."));
    }
    public String getNameOfMethods(String declaration){
        int startIndex = declaration.indexOf(" ")+1;
        int endIndex = declaration.indexOf("(",startIndex);
        return declaration.substring(startIndex,endIndex);
    }

    public HashSet<String> getTypeOfParametersOfMethod(String declaration){
        int startIndex = declaration.indexOf("(")+1;
        int endIndex = declaration.indexOf(")",startIndex);
        String parameters = declaration.substring(startIndex,endIndex);
        HashSet<String> set = new HashSet<>();
        if (!parameters.isEmpty()){
            int firstStart = 0;
            int firstEnd = parameters.indexOf(" ",firstStart);
            String sub = parameters.substring(firstStart,firstEnd);
            set.add(sub);
            int i = parameters.indexOf(",");
            if (i == -1) return set;
            int j = i+1;
            while (j < parameters.length()){

                i=parameters.indexOf(",",i);
                if (i == -1) return set;
                j = i+1;
            }
        }

        return set;
    }

    public String getNameOfAttributes(String declaration){
        int classIndex=declaration.indexOf(" ");
        int typeIndex = classIndex+1;
        int startIndex = declaration.indexOf(" ",typeIndex)+1;
        int endIndex = declaration.indexOf(";",startIndex);
        return declaration.substring(startIndex,endIndex);
    }

    public String getNameOfClass(String declaration){
        int classIndex=declaration.indexOf("class");
        int nameIndex = classIndex+6;
        int endIndex = declaration.indexOf(" ",nameIndex);
        return declaration.substring(nameIndex,endIndex);
    }
    @Override
    public void visit(EnumDeclaration declaration, Void arg) {
        System.out.println("Enumeration");
        visitTypeDeclaration(declaration, arg);
    }

    public int getCCofMethod(MethodDeclaration declaration){
        int somme = 1;
        //Get body of method
        //Analyse if there are some ifs/elses/for/while
        String body = declaration.toString().substring(declaration.toString().indexOf("{")+1,declaration.toString().indexOf("}"));
        HashSet<String> words = new HashSet<>();
        int i = 0;
        while (i < body.length()){
            int endIndex = body.indexOf(" ",i);
            words.add(body.substring(i,endIndex));
            i = endIndex;
        }
        for (String s : words){
            if (s == "if" || s == "else" || s=="while"|| s=="for") somme++;
        }
        return somme;
    }

    /**
     * Print pour les méthodes
     * @param declaration
     * @param arg
     */
    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        String nameOfMethod = getNameOfMethods(declaration.getDeclarationAsString(false,false));
        tabMethods.get(indexPackage).get(indexClass).add(nameOfMethod);
        tabCC.get(indexPackage).get(indexClass).add(getCCofMethod(declaration));
        System.out.println("Declaration : "+getTypeOfParametersOfMethod(declaration.getDeclarationAsString(false,false)));
    }

    public String getReportCC(){
        String print = "";
        System.out.println(tabCC);
        for (int j = 0; j < tabPackages.size();j++) {
            print+=(tabPackages.get(j)+"\n");
            for (int i = 0; i < tabMethods.get(j).size(); i++) {
                print += ("  " + tabClassName.get(j).get(i));
                for (String s : tabMethods.get(j).get(i)) {
                    print+="\n"+s+" : "/*+tabCC.get(j).get(i)*/;
                }
            }
        }
        return print.substring(0, print.length()-2);
    }

    public void setFileName(String s){
        this.fileName = s;
    }
}
