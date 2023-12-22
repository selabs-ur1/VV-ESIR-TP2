package fr.istic.vv;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.nodeTypes.NodeWithDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;


public class PrivateFieldsPrinter extends VoidVisitorWithDefaults<Void> {

    private ArrayList<MethodDeclaration> publicMethods = new ArrayList<MethodDeclaration>();
    private ArrayList<FieldDeclaration> privateFields = new ArrayList<FieldDeclaration>();

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        //if(!declaration.isPublic()) return;
        //System.out.println(declaration.getFullyQualifiedName().orElse("[Anonymous]"));
        for(MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
            //add public methods to list
            if(method.isPublic())
                publicMethods.add(method);
        }

        for(FieldDeclaration field : declaration.getFields()) {
            field.accept(this, arg);
            //add private fields to list
            if(field.isPrivate())
                privateFields.add(field);
        }
        
        // nested types in the top level
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
    public void visit(MethodDeclaration declaration, Void arg) {
        if(!declaration.isPublic()) return;
    }

    //print private attributes
    @Override
    public void visit(FieldDeclaration declaration, Void arg) {
        if(!declaration.isPrivate()) return;
    }

    //print package name
    @Override
    public void visit(PackageDeclaration declaration, Void arg) {
        
    }




        //return a list of all private fields without public getter: type, name and the class of wich they are a parameter
        public ArrayList<String> getPrivateAttributesNamesWithoutPublicGetter() {
            ArrayList<String> privateAttributesNamesWithoutPublicGetterAndTheirClassName = new ArrayList<String>();
            //print all private fields without public getter
              for(FieldDeclaration field : privateFields) {
                   //get field name
                   String fieldName = field.getVariable(0).getNameAsString();
                   //check if there is a public getter
                   boolean hasPublicGetter = false;
                   for(MethodDeclaration method : publicMethods) {
                     //check if method respect the getter naming convention
                     if(method.getNameAsString().equals("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1))) {
                         //if the return type is the same as the field type
                         if(method.getTypeAsString().equals(field.getVariable(0).getTypeAsString())) {
                             hasPublicGetter = true;
                             break;
                         }
                     }
                   }
                   //add field if there is no public getter
                   if(!hasPublicGetter)
                   {
                            //get package name 
                        String packageName = "";
                        if(field.getParentNode().get().getParentNode().get() instanceof CompilationUnit)
                            packageName = ((CompilationUnit) field.getParentNode().get().getParentNode().get()).getPackageDeclaration().get().getNameAsString();
                        else
                            packageName = "none";
                        //field type, field name, class name, class package
                        privateAttributesNamesWithoutPublicGetterAndTheirClassName.add(field.getVariable(0).getTypeAsString() + " " 
                        + fieldName + " " + ((NodeWithSimpleName<MethodDeclaration>) field.getParentNode().get()).getNameAsString() + " " + packageName
                        );
                        
                        //field lines if needed : field.getRange().get().begin.line + " " + field.getRange().get().end.line
                   }
              }
              return privateAttributesNamesWithoutPublicGetterAndTheirClassName;
         }

         //generate a html report from a list of string and a name
            public void generateHtmlReport(ArrayList<String> list, String name) {
                //remove the path from the name
                String fileName = name.substring(name.lastIndexOf('/') + 1);
                String html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<style>\n" +
                "table {\n" +
                "  font-family: arial, sans-serif;\n" +
                "  border-collapse: collapse;\n" +
                "  width: 100%;\n" +
                "}\n" +
                "\n" +
                "td, th {\n" +
                "  border: 1px solid #dddddd;\n" +
                "  text-align: left;\n" +
                "  padding: 8px;\n" +
                "}\n" +
                "\n" +
                "tr:nth-child(even) {\n" +
                "  background-color: #dddddd;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<h2>" + fileName + "</h2>\n" +
                "\n" +
                "<table>\n" +
                "  <tr>\n" +
                "    <th>Type</th>\n" +
                "    <th>Name</th>\n" +
                "    <th>Class</th>\n" +
                "    <th>Package</th>\n" +
                "  </tr>\n";
                for(String s : list) {
                    String[] split = s.split(" ");
                    html += "  <tr>\n" +
                    "    <td>" + split[0] + "</td>\n" +
                    "    <td>" + split[1] + "</td>\n" +
                    "    <td>" + split[2] + "</td>\n" +
                    "    <td>" + split[3]  + "</td>\n" +
                    "  </tr>\n";
                }
                html += "</table>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
                //Save html file
                try {
                    PrintWriter out = new PrintWriter(name);
                    out.println(html);
                    out.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
}
