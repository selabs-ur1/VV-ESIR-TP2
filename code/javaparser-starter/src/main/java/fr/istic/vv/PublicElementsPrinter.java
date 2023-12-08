package fr.istic.vv;

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
        System.out.println(declaration.getFullyQualifiedName().orElse("[Anonymous]"));

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

    public void addPrivateField(FieldDeclaration declaration) {
        if(declaration.isPublic()) return;
        getVariableName(declaration.toString());
        //System.out.println("  " + declaration.toString());
    }

    public void getVariableName (String declaration){

        String tmp = "";

        Pattern pattern = Pattern.compile("\\s(\\w+);");
        Matcher matcher = pattern.matcher(declaration);
        if (matcher.find())
        {
            tmp = matcher.group(1);
        }

        String firstLetter = (tmp.charAt(0) + "").toUpperCase();
        tmp = "get" + firstLetter + tmp.substring(1) + "()";

        System.out.println(tmp);
        privateAttribut.add(tmp);
        
    }

}
