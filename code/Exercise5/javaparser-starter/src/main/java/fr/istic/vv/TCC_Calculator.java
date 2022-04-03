package fr.istic.vv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static guru.nidi.graphviz.model.Factory.*;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.LinkSource;

public class TCC_Calculator extends VoidVisitorWithDefaults<Void> {

	private Set<String> attributClass;
	private Map<String, HashSet<String>> attributMethod;
	private String packageDeclaration;
	private String className;
	private double tcc;
	private Map<String, Double> tccPerClass;
	private Map<String, Set<String>> graph;

	public TCC_Calculator() {
		tccPerClass = new HashMap<>();
	}

	@Override
	public void visit(CompilationUnit unit, Void arg) {
		if (unit.getPackageDeclaration().isPresent()) {
			packageDeclaration = unit.getPackageDeclaration().get().toString();
			packageDeclaration = packageDeclaration.replaceAll("[\n;]", "").replace("package ", "");
		} else {
			packageDeclaration = "no package found";
		}

		for (TypeDeclaration<?> type : unit.getTypes()) {
			className = type.getName().asString();
			// System.out.println(className);
			type.accept(this, null);
		}
	}

	public void drawTCC() {
		Graph graphSVG = graph("example1").linkAttr().with("class", "link-class");
		List<LinkSource> links = new ArrayList<>();

		for (String key : graph.keySet()) {
			if (graph.get(key).size() == 0) {
				links.add(node(removeChars(key)));
			} else {
				for (String edge : graph.get(key)) {
					links.add(node(removeChars(key)).link(removeChars(edge)));
				}
			}

		}

		graphSVG = graphSVG.with(links);

		try {
			Graphviz.fromGraph(graphSVG).render(Format.SVG)
					.toFile(new File("SVG/" + className + ".svg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int[] histogram(int nbCat) {
		int[] hist = new int[nbCat];
		double seuilMin = 0;
		double seuilMax = 0;

		for (int i = 0; i < nbCat; i++) {
			seuilMin = i * ((double) 1 / nbCat);
			seuilMax = (i + 1) * ((double) 1 / (nbCat));

			for (Entry<String, Double> tmp : tccPerClass.entrySet()) {
				if (tmp.getValue() >= seuilMin && tmp.getValue() < seuilMax) {
					hist[i]++;
				}
			}
		}

		return hist;
	}

	public void printTccReport() {
		FileWriter fw;
		try {
			fw = new FileWriter("/home/jce/TCC.txt", true);
			BufferedWriter writer = new BufferedWriter(fw);

			writer.write("Package :\t" + packageDeclaration);
			writer.newLine();
			writer.write("Class :\t\t" + className);
			writer.newLine();
			writer.write("TCC :\t\t" + Math.round(tcc * 100.0) / 100.0);
			writer.newLine();
			writer.newLine();

			writer.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public double computeTCC() {
		graph = new HashMap<>();
		int nbMethod = attributMethod.keySet().size();
		int maxEdge = nbMethod * (nbMethod - 1) / 2;
		int nbEdge = 0;

		for (String m1 : attributMethod.keySet()) {
			graph.put(m1, new HashSet<String>());
			for (String m2 : attributMethod.keySet()) {
				if (m1.compareTo(m2) != 0) {
					Set<String> intersection = new HashSet<String>(attributMethod.get(m1));
					intersection.retainAll(attributMethod.get(m2));

					if (intersection.size() != 0 && graph.get(m2) != null && !graph.get(m2).contains(m1)) {
						nbEdge++;
						graph.get(m1).add(m2);
					}
				}
			}
		}

		// graphtoDot(className);
		drawTCC();

		graph = new HashMap<>();
		nbEdge /= 2;

		return ((double) nbEdge / (double) maxEdge);
	}

	public void visitClassDeclaration(TypeDeclaration<?> declaration, Void arg) {
		attributMethod = new HashMap<>();
		attributClass = new HashSet<String>();

		for (BodyDeclaration<?> member : declaration.getMembers()) {
			if (member instanceof FieldDeclaration) {
				member.accept(this, arg);
			}
		}

		for (BodyDeclaration<?> member : declaration.getMembers()) {
			if (member instanceof MethodDeclaration) {
				member.accept(this, arg);
			}
		}

		tcc = computeTCC();
		tccPerClass.put(className, tcc);
		printTccReport();
	}

	@Override
	public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
		visitClassDeclaration(declaration, arg);
	}

	@Override
	public void visit(FieldDeclaration declaration, Void arg) {
		List<VariableDeclarator> name = declaration.getVariables();

		for (VariableDeclarator tmp : name) {
			attributClass.add(tmp.getNameAsString());
		}
	}

	@Override
	public void visit(MethodDeclaration declaration, Void arg) {
		String methodName = declaration.getNameAsString();

		if (declaration.getParameters() != null) {
			methodName += declaration.getParameters().toString();
		}

		methodName = methodName.replace('[', '(').replace(']', ')');

		attributMethod.put(methodName, new HashSet<String>());

		for (SimpleName member : declaration.findAll(SimpleName.class)) {
			if (attributClass.contains(member.asString())) {
				attributMethod.get(methodName).add(member.asString());
			}
		}

	}

	private String removeChars(String input) {
		return input.replace("(", " ").replace("<", "").replace(">", "").replace(",", "").replace(")", " ")
				.replace(".", " ").replaceAll("strict", "").replaceAll(" ", "");
	}
	
	public void printHist(int[] hist, double scaleFactor) {
		System.out.println("\n\nHistogramme : \n");
		int nbClass = 0;
		
		for (int i = 0; i < hist.length; i++) {
			double seuilMin = i * ((double) 1 / hist.length);
			seuilMin = Math.round(seuilMin * 100.0) / 100.0;
			double seuilMax = (i + 1) * ((double) 1 / hist.length);
			seuilMax = Math.round(seuilMax * 100.0) / 100.0;
			
			nbClass += hist[i];
			if(i != hist.length-1) {
				System.out.println("[" + seuilMin + " , " + seuilMax + "[ (" + hist[i] +
						") :" + "\t".repeat(1) + "*".repeat((int)(hist[i]*scaleFactor)));
			}
			else {
				System.out.println("[" + seuilMin + " , " + seuilMax + "]" + " (" + hist[i] +
						") :" + "\t".repeat(1) + "*".repeat((int)(hist[i]*scaleFactor)));
			}
			
		}
		
		System.out.println("nombre de class : " + nbClass);
	}
}
