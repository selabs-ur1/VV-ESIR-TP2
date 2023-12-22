package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import javax.xml.transform.stream.StreamSource;
import java.sql.SQLOutput;
import java.util.*;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {



    public int indexClass=-1;
    public int indexPackage =-1;

    private String fileName="";

    public final List<String> packageName; //liste des noms de package
    public final List<List<HashSet<String>>> tabAttributes; //liste des noms d'attributs (package/class/attributs)
    public final List<List<HashSet<String>>> tabGetters; //liste des noms de getters (package/class/getters)
    public final List<List<String>> tabClassName;   //liste des classNames (package/class)

    public PublicElementsPrinter(){
        tabAttributes= new ArrayList<>(); //Init la liste (package)
        tabGetters= new ArrayList<>();  //Init la liste (package)
        tabClassName=new ArrayList<>(); //Init la liste (package)
        packageName=new ArrayList<>(); //Init la liste de package
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
        if(!declaration.isPublic()) {
            System.out.println("Privé !");
            return;
        }
        HashSet<String> attributes = new HashSet<>();  //Creation of an attributes set
        for (int i = 0; i < declaration.getFields().size();i++){
            if (declaration.getFields().get(i).isPrivate()){ //Checks if the attribute is private
                attributes.add(getNameOfAttributes(declaration.getFields().get(i).toString()));
            }
        }
        tabAttributes.get(indexPackage).set(indexClass,attributes); //Set
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
        String packageOfDeclaration = getPackageName(declaration.getFullyQualifiedName().toString());   //Get the packageName of the declaration
        if (packageName.isEmpty() || !(packageName.get(indexPackage).equals(packageOfDeclaration+" : "+fileName))) {    //if the set of packageName is empty (at start), add the packageName to the set at index 0.
            indexPackage++;
            packageName.add(indexPackage,packageOfDeclaration+" : "+fileName);    //Add the new package to the package list
            tabClassName.add(indexPackage,new ArrayList<>());  //Init a new list at index (indexPackage) (package/class) to get classes of package
            tabGetters.add(indexPackage, new ArrayList<>()); //Init a new list at index (indexPackage) (package/class/getters) to add all getters from package
            tabAttributes.add(indexPackage, new ArrayList<>()); //Init a new list at index (indexPackage) (package/class/getters) to add all attributes from package
            indexClass=-1;
        }
        //WE HAVE TO CHECK THE NAME OF THE CLASS (IF IT IS DIFFERENT OR NOT)
        String classOfDeclaration = getNameOfClass(declaration.toString());

        if (tabClassName.get(indexPackage).isEmpty()){                                              //If the classlist is empty (at start of new package)
            tabClassName.add(indexPackage,new ArrayList<>());                                       //create a new arrayList
        } else if (!(tabClassName.get(indexPackage).get(indexClass).equals(classOfDeclaration))){   //If the class of Declaration is different from the last class saved, add the new class to the list

        }
        indexClass++;
        tabClassName.get(indexPackage).add(indexClass, classOfDeclaration);                         //add the class of Declaration to the set
        tabAttributes.get(indexPackage).add(indexClass, new HashSet<>());                           //
        tabGetters.get(indexPackage).add(indexClass, new HashSet<>());                              //
        tabClassName.get(indexPackage).add(getNameOfClass(declaration.toString()));                 //Add the name of class of Declaration to the array of classNames.

        visitTypeDeclaration(declaration, arg);                                                     //
    }

    public String getPackageName(String declaration){
        return declaration.substring(declaration.indexOf("[")+1,declaration.indexOf("."));
    }
    public String getNameOfMethods(String declaration){
        int startIndex = declaration.indexOf(" ")+1;
        int endIndex = declaration.indexOf("(",startIndex);
        return declaration.substring(startIndex,endIndex);
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

    /**
     * Print pour les méthodes
     * @param declaration
     * @param arg
     */
    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        if(!declaration.isPublic()) {System.out.println("Une méthode priveé");return;}
        String nameOfMethod = declaration.getDeclarationAsString(false,false);
        int positionOfSpace = nameOfMethod.indexOf(" ");
        if (nameOfMethod.substring(positionOfSpace+1,positionOfSpace+4).equals("get")){
            //System.out.println("  //Je suis un getter !");

            tabGetters.get(indexPackage).get(indexClass).add((getNameOfMethods(declaration.getDeclarationAsString(false,false))));
        }
    }

    public boolean isThereAFieldWithoutGetter (){
        if (tabGetters.get(indexPackage).get(indexClass).size()<tabAttributes.get(indexPackage).get(indexClass).size()) {
            System.out.println("Il y a moins de getters que d'attributs pour la classe " + tabClassName.get(indexPackage).get(indexClass));
            return false;
        }
        return true;
    }

    public String getFieldsWithoutGetter(){
        String print = "";

        for (int j = 0; j < tabAttributes.size();j++) {
            print+=(packageName.get(j)+"\n");
            for (int i = 0; i < tabAttributes.get(j).size(); i++) {
                print += ("  " + tabClassName.get(j).get(i));
                for (String s : tabAttributes.get(j).get(i)) {
                    String firstLetter = "" + s.charAt(0);
                    if (!tabGetters.get(j).get(i).contains("get" + firstLetter.toUpperCase() + s.substring(1))) {
                        print += ("\n    L'attribut " + s + " n'a pas de getter\n\n");
                    }
                }
            }
        }
        return print.substring(0, print.length()-2);
    }

    public void setFileName(String s){
        this.fileName = s;
    }
}
