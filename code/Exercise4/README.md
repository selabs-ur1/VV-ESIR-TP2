# Code of your exercise

package fr.istic.vv;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {

    List<String> privateAttribut = new ArrayList<String>();

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if(!declaration.isPublic()) return;

        //parcours tous les attributs
        for(FieldDeclaration field : declaration.getFields()){
            addPrivateField(field);
        }

        for(MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }
        
        // Printing nested types in the top level
        for(BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration)
                member.accept(this, arg);
        }

        try {
            FileWriter writer = new FileWriter("./code/javaparser-starter/src/main/java/fr/istic/vv/report.txt");
            writer.flush();
            if (privateAttribut.isEmpty()){
                writer.write("Aucun problemes dans votre code, continuez comme ca !\r\n");
            }
            else{
                writer.write("oh oh, il semblerait que les attributs suivants n'aient pas de getters :\r\n");
                for (String s : privateAttribut){
                    writer.write("-" + s + "\r\n");
                }
            }
            writer.close();
        } catch (IOException e) {
        }
        System.out.println(privateAttribut.toString());
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
        String functionName = declaration.getNameAsString();
        if (privateAttribut.indexOf(functionName) != -1){
            privateAttribut.remove(privateAttribut.indexOf(functionName));
        }
    }

    /**
     * Recupere la declaration d'une variable, si cette derniere est privee alors elle sera traitee.
     * @param declaration
     * La declaration complete d'une variable
     */
    public void addPrivateField(FieldDeclaration declaration) {
        if(declaration.isPublic()) return;
        getVariableName(declaration.toString());
        //System.out.println("  " + declaration.toString());
    }

    /**
     * Recupere la declaration d'une variable et ajoute le nom de cette derniere dans la liste privateAttribut.
     * @param declaration
     * La declaration complete de la variable.
     */
    public void getVariableName (String declaration){

        String tmp = "";

        Pattern pattern = Pattern.compile("\\s(\\w+);");
        Matcher matcher = pattern.matcher(declaration);
        if (matcher.find())
        {
            tmp = matcher.group(1);
        }

        String firstLetter = (tmp.charAt(0) + "").toUpperCase();
        tmp = "get" + firstLetter + tmp.substring(1);

        privateAttribut.add(tmp);
        
    }

}
