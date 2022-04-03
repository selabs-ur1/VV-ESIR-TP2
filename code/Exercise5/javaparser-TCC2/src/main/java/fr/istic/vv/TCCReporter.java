package fr.istic.vv;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter; // Import the FileWriter class

/**
 * This class has the purpose of calculating the TCC in classes, But due to my
 * lack of knowledge in javaparser I might have complicated my task.
 * 
 * (along with other things)One of the main reason why it is no optimal is
 * because we verify if a variable is used in another method by checking if the
 * String of the method contains the name of the variable. Which can lead to
 * overestimating the TCC.
 */

public class TCCReporter extends VoidVisitorWithDefaults<Void> {

	public FileWriter TCCWriter = null;
	String className;// TODO retrieve className
	double NP = 0;// maximum number of connections
	Map<String, Set<String>> metVar;
	Map<String, Set<String>> methodLinkedTo;
	double TCC = 0;
	double NDC=0;

	Set<String> classField;

	@Override
	public void visit(CompilationUnit unit, Void arg) {

		// get the current package
		PackageDeclaration p = unit.findAll(PackageDeclaration.class).get(0);

		metVar = new HashMap<String, Set<String>>(); // nomDeMethod --> set contenant les variables utilisées
		methodLinkedTo = new HashMap<String, Set<String>>(); // nomDeMethod --> set contenant les methodes liées

		classField = new HashSet<String>();// set containing all the class field

		try {
			TCCWriter = new FileWriter(System.getProperty("user.dir") + "\\TCCReport.csv", true);

			// iteration through all the typeDeclaration of the current class
			for (TypeDeclaration<?> type : unit.getTypes()) {
				type.accept(this, null);
			}

			double classNumber = metVar.size();
			NP = (classNumber * (classNumber - 1.0)) / 2.0;// computes the max number of connections

			List<Entry<String, Set<String>>> mapList = new ArrayList<Map.Entry<String, Set<String>>>(metVar.entrySet());
			int currentEntryIndex = 1;
			for (Map.Entry<String, Set<String>> mainEntry : mapList) {

				Set<String> setMethod = new HashSet<String>();// set containing all the method linked to the mainEntry
																// method

				// create a sublist of the main set
				List<Map.Entry<String, Set<String>>> subList = new ArrayList<Map.Entry<String, Set<String>>>(mapList);
				subList = subList.subList(currentEntryIndex, mapList.size());

				for (Map.Entry<String, Set<String>> currEntry : subList) {
					Set<String> testSet = new HashSet<String>(mainEntry.getValue());
					int sizeBefore = testSet.size();
					// System.out.println(currEntry.getKey());

					testSet.removeAll(currEntry.getValue());
					if (testSet.size() < sizeBefore) {
						// if the substraction of the two set has an impact on the first set, it means
						// that the
						// two methods have some common variables, and therefore are linked
						setMethod.add(currEntry.getKey());
					}

				}
				currentEntryIndex++;
				// once we have iterated througth all the methods after the current method we
				// can add the set to our map
				methodLinkedTo.put(mainEntry.getKey(), setMethod);
			}

			NDC = 0;

			for (Map.Entry<String, Set<String>> sumEntry : methodLinkedTo.entrySet()) {
				NDC += sumEntry.getValue().size();
			}

			TCC = NDC / NP;
			if (!Double.isNaN(TCC) && TCC != 0) {
				TCCWriter.append(p.getNameAsString() + "," + className + "," + TCC + "\n");
			}

			TCCWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		// creating the graph
		// creating the file
		if (NDC != 0) {
			try {
				File myObj = new File("GraphFolder\\" + className + "Graph.dot");
				myObj.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// writting the graph
			try {
				FileWriter myWriter = new FileWriter("GraphFolder\\" + className + "Graph.dot");
				myWriter.write("strict graph ip_map {\n");
				for (Map.Entry<String, Set<String>> graphEntry : methodLinkedTo.entrySet()) {
					for (String s : graphEntry.getValue()) {
						myWriter.write(graphEntry.getKey() + "--" + s + ";\n");
					}
				}
				myWriter.write("}");
				myWriter.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
		if (!declaration.isPublic())
			return;
		// System.out.println(declaration.getFullyQualifiedName().orElse("[Anonymous]"));
		for (MethodDeclaration method : declaration.getMethods()) {
			method.accept(this, arg);
		}
		// Printing nested types in the top level
		for (BodyDeclaration<?> member : declaration.getMembers()) {
			member.accept(this, arg);
		}
	}

	@Override
	public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
		if (declaration.isAbstract())
			return;
		className = declaration.getNameAsString();
		// System.out.println(className);
		visitTypeDeclaration(declaration, arg);
	}

	@Override
	public void visit(FieldDeclaration declaration, Void arg) {
		for (VariableDeclarator v : declaration.getVariables()) {
			classField.add(v.getNameAsString());
		}
	}

	@Override
	public void visit(EnumDeclaration declaration, Void arg) {
		visitTypeDeclaration(declaration, arg);
	}

	@Override
	public void visit(MethodDeclaration declaration, Void arg) {
		Set<String> variablesUsed = new HashSet<String>();
		for (FieldAccessExpr F : declaration.findAll(FieldAccessExpr.class)) {
			variablesUsed.add(F.getNameAsString());
		}
		for (NameExpr F : declaration.findAll(NameExpr.class)) {
			variablesUsed.add(F.getNameAsString());
		}

		variablesUsed.retainAll(classField);
		metVar.put(declaration.getNameAsString(), variablesUsed);
	}

}
