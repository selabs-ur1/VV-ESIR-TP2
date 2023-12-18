package fr.istic.vv;

import java.util.ArrayList;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {

    // Initializing arraylist for private variables without getter
    private ArrayList<FieldDeclaration> privateVarWithoutGetter = new ArrayList<>();

    // Initializing arraylist for getters
    private ArrayList<MethodDeclaration> getters = new ArrayList<>();

    // Initializing arraylist for class names
    private ArrayList<String> classNames = new ArrayList<>();

    // Initializing arraylist for class names
    private ArrayList<String> classNamesMethods = new ArrayList<>();

    // Initializing arraylist for package names
    private ArrayList<String> packageNames = new ArrayList<>();

    // Initializing arraylist for method names
    private ArrayList<MethodDeclaration> methods = new ArrayList<>();

    // Initializing parameter of methods
    private ArrayList<String> parameters = new ArrayList<>();

    // Initializing number of methods
    private int numberOfMethods = 0;

    // Initializing cyclomatic complexity for each method
    private int[] CyclomaticComplexity;

   
    // Getters
    public ArrayList<String> getPackageNames() {return packageNames;}
    public ArrayList<String> getClassNames() {return classNames;}
    public ArrayList<MethodDeclaration> getGetters() {return getters;}
    public ArrayList<FieldDeclaration> getPrivateVarWithoutGetter() {return privateVarWithoutGetter;}
    public int[] getCyclomaticComplexity() {return CyclomaticComplexity;}
    public ArrayList<MethodDeclaration> getMethod() {return methods;}
    public ArrayList<String> getParameters() {return parameters;}
    public ArrayList<String> getClassNamesMethods() {return classNamesMethods;}

    @Override
    public void visit(CompilationUnit unit, Void arg) { 

        // Looping through all methods
        int i = 0;
        for(MethodDeclaration method : unit.findAll(MethodDeclaration.class)) {
            numberOfMethods++;

            // Checking if method is getter
            if(method.getNameAsString().startsWith("get")){
                getters.add(method);
            }

            // Adding method names to arraylist
            methods.add(method);

            classNamesMethods.add(unit.getClassByName(method.getNameAsString()).toString());

            // Adding parameters to arraylist
            parameters.add(method.getParameters().toString());
        }


        // Initializing array for cyclomatic complexity : each method has a cyclomatic complexity of 1
        CyclomaticComplexity = new int[numberOfMethods];
        for(int q = 0; q < numberOfMethods; q++){
            CyclomaticComplexity[q] = 1;
        }

        i = 0;

        // Looping through all methods
        for(MethodDeclaration method : unit.findAll(MethodDeclaration.class)) {

            // Checking if method has a body
            if(method.getBody().isPresent()){

                // Getting body of method
                String body = method.getBody().get().toString();

                // Counting number of if, for, while, case, catch, &&, || in the body
                int ifCount = body.split("if").length - 1;
                int forCount = body.split("for").length - 1;
                int whileCount = body.split("while").length - 1;
                int caseCount = body.split("case").length - 1;
                int catchCount = body.split("catch").length - 1;
                int andCount = body.split("&&").length - 1;
                int orCount = body.split("\\|\\|").length - 1;
                int cc = ifCount + forCount + whileCount + caseCount + catchCount + andCount + orCount;

                // Adding cyclomatic complexity to array
                CyclomaticComplexity[i] += cc;
                i++;
            }
        }

        // Looping through all package names
        for(PackageDeclaration pack : unit.findAll( PackageDeclaration.class)) {
            packageNames.add(pack.getNameAsString());
        }

        // Looping through all private variables without getter
        for(TypeDeclaration<?> type : unit.getTypes()) {
            for(FieldDeclaration field : type.getFields()) {

                // Checking if variable is private
                if(field.isPrivate()){

                    // Looping through all methods
                    for(MethodDeclaration method : type.getMethods()){

                        // Checking if method is getter
                        if(!(method.getNameAsString().toLowerCase().equals("get"+field.getVariable(0).getNameAsString().toLowerCase()))){
                            
                            // Adding private variable without getter to arraylist
                            privateVarWithoutGetter.add(field);
                            classNames.add(type.getNameAsString());
                        }
                    }
                }
            }
            
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
            if (member instanceof TypeDeclaration)
                member.accept(this, arg);
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
        if(!declaration.isPublic()) return;
        System.out.println("  " + declaration.getDeclarationAsString(true, true));
    }

}
