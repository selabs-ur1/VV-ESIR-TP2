package fr.istic.vv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {
	private double nbMaxConnections = 0.0;
	private Set<String> setFieldsName;
	private Map<String, Set<String>> mapMethodsVariables;
	private String[] dataRow = new String[3];
	
	private List<String[]> dataForCSV = new ArrayList<String[]>();
	
	@Override
	public void visit(CompilationUnit unit, Void arg) {
		for (TypeDeclaration<?> type : unit.getTypes()) {
			dataRow[0]=unit.getPackageDeclaration().get().getNameAsString();
			type.accept(this, null);
			dataForCSV.add(dataRow);
			dataRow = new String[3];
		}
	}

	public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
		if (!declaration.isPublic())
			return;
		dataRow[1]=declaration.getNameAsString();
		//System.out.println("  In class :" + declaration.getNameAsString());

		for (MethodDeclaration method : declaration.getMethods()) {
			//get All variables used in a method
			List<NameExpr> listNameExpr = method.findAll(NameExpr.class);
			ListIterator<NameExpr> itNameExpr = listNameExpr.listIterator();
			
			//get All accessed from scope in method
			List<FieldAccessExpr> listFieldAccessExpr = method.findAll(FieldAccessExpr.class);
			ListIterator<FieldAccessExpr> itFieldAccess = listFieldAccessExpr.listIterator();

			List<String> listNameExprString = new ArrayList<String>();

			// variables used in method
			while (itNameExpr.hasNext()) {
				NameExpr current = itNameExpr.next();
				listNameExprString.add(current.getNameAsString());
			}

			// variable accessed from scope in method
			while (itFieldAccess.hasNext()) {
				FieldAccessExpr current = itFieldAccess.next();
				listNameExprString.add(current.getNameAsString());
			}
			// Keep only attributes from the class
			listNameExprString.retainAll(setFieldsName);
			Set<String> varSet = new HashSet<String>(listNameExprString);
			// Add a set of variables from a method that is the key
			mapMethodsVariables.put(method.getNameAsString(), varSet);
			// Count the number of methods
			nbMaxConnections++;
			method.accept(this, arg);
			// System.out.println("nbco "+nbMaxConnections);

			List<FieldDeclaration> listFields = declaration.getFields();
			ListIterator<FieldDeclaration> it = listFields.listIterator();

			// Add all the names of the variable in a list in the current class
			while (it.hasNext()) {
				FieldDeclaration current = it.next();
				setFieldsName.add(current.getVariable(0).getNameAsString());
			}

		}

		nbMaxConnections = (nbMaxConnections * (nbMaxConnections - 1)) / 2;
		
		double tcc = 0.0; 
		if(nbMaxConnections!=0) {
			tcc=(directConnections()/nbMaxConnections);
		}
		dataRow[2]= ""+tcc;
		//System.out.println("    TCC = " + directConnections() + "/" + nbMaxConnections);
		nbMaxConnections = 0;
		// Printing nested types in the top level
		for (BodyDeclaration<?> member : declaration.getMembers()) {
			if (member instanceof TypeDeclaration)
				member.accept(this, arg);
		}
	}

	@Override
	public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
		if (!declaration.isPublic())
			return;
		setFieldsName = new HashSet<String>();
		mapMethodsVariables = new HashMap<String, Set<String>>();

		List<FieldDeclaration> listFields = declaration.getFields();
		ListIterator<FieldDeclaration> it = listFields.listIterator();

		setFieldsName = new HashSet<String>();
		mapMethodsVariables = new HashMap<String, Set<String>>();

		// Add all the names of the variable in a list in the current class
		while (it.hasNext()) {
			FieldDeclaration current = it.next();
			setFieldsName.add(current.getVariable(0).getNameAsString());
		}
		visitTypeDeclaration(declaration, arg);

	}

	@Override
	public void visit(EnumDeclaration declaration, Void arg) {
		visitTypeDeclaration(declaration, arg);
	}

	@Override
	public void visit(MethodDeclaration declaration, Void arg) {
		if (!declaration.isPublic())
			return;
	}

	/**
	 * Return the number of direct connections of methods in a class
	 * 
	 * @return an int of the number of comparison
	 */
	public double directConnections() {
		double res = 0.0;
		// Index of the current method variables
		int primaryIndex = 0;

		// Current set of variables
		for (Set<String> setMethodVariables : mapMethodsVariables.values()) {
			// Index of the method variables to compare
			int secondaryIndex = 0;
			// Other sets of variables
			for (Set<String> setOtherMethodVariables : mapMethodsVariables.values()) {
				// Verify if we don't do the same comparison multiple times
				if (secondaryIndex > primaryIndex) {
					Set<String> intersection = new HashSet<String>(setMethodVariables);
					intersection.retainAll(setOtherMethodVariables);
					if (!intersection.isEmpty()) {
						res++;
					}
				}
				secondaryIndex++;
			}
			primaryIndex++;
		}
		return res;
	}
	
	public List<String[]> getDataForCSV(){
		String[] header = {"Package", "Class Name", "TCC value" };
		 dataForCSV.add(0,header);
		 return dataForCSV;
	}
}