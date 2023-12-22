package fr.istic.vv;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import com.github.javaparser.utils.Pair;

/**
 * Make a report of a java project
 */
public class ReportMaker{

    private ArrayList<Pair<String, Integer>> cyclomaticComplexity = new ArrayList<>();


    public ReportMaker(MethodInfoNode infos, String filename) throws IOException {
        makeReport(infos, filename);
    }

    /**
     * Create a report in CSV format
     * @return the report
     */
    private void makeReport(MethodInfoNode infos, String filename) throws IOException{
        //INITIALISATION//
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        writer.write("Class, Methods, Cyclomatic Complexity, Parameters");
        System.out.println("Class, Methods, Cyclomatic Complexity, Parameters");
        
        //VISIT//
        for (MethodInfoNode classe : infos.getAllChilds()) {// visit class
            int currentComplexity = -1;
            int complexityByClass = 0;
            if(classe.getNbChilds() > 0) {
                for (MethodInfoNode method : classe.getAllChilds()) { // visit methods
                    try { // if the method is not a method but a cyclomatic complexity
                        currentComplexity = Integer.parseInt(method.getValue());
                        complexityByClass += currentComplexity;
                    } catch(Exception e){
                        StringBuffer line = new StringBuffer(classe.getValue() + ", " + method.getValue() + ", " + currentComplexity + ", ");
                        if(method.getNbChilds() > 0) {
                            for (MethodInfoNode param : method.getAllChilds()) { // visit parameters
                                line.append(param.getValue()+"/");
                            }
                        } else {
                            line.append("none");
                        }
                        //Write and display
                        System.out.println(line.toString());
                        writer.newLine();
                        writer.write(line.toString());
                    }
                }
                cyclomaticComplexity.add(new Pair<String,Integer>(classe.getValue(), complexityByClass));
            }
        }
        writer.close();
    }    

    /**
     * Displays the histogram of the report for each class on STDOUT
     */
    public void histogramByClass() {
        //INITIALISATION//
        if (cyclomaticComplexity.size() == 0) return;

        int maxLengthStr = 0;
        int maxLengthComplexity = 0;

        //PRE-PROCESS//
        for (Pair<String,Integer> pair : cyclomaticComplexity) {
            if (pair.a.length() > maxLengthStr) maxLengthStr = pair.a.length();
            if (pair.b > maxLengthComplexity) maxLengthComplexity = pair.b;
        }
        maxLengthStr += 2;
        
        //PROCESS//
        for (Pair<String,Integer> pair : cyclomaticComplexity) {
            StringBuffer res = new StringBuffer();
            res.append(pair.a);
            int dif = maxLengthStr - pair.a.length();
            for (int i=0; i<dif; ++i)
                res.append(" ");
            res.append("| ");
            for (int i=0; i<pair.b; ++i)
                res.append("▄");
            res.append(" ");
            res.append(pair.b);
            System.out.println(res.toString());
        }
    }

    /**
     * Display the complexity pourcentage of each package of total project complexity on STDOUT
     */
    public void histogramByPackage() {
        //INITIALISATION//
        if (cyclomaticComplexity.size() == 0) return;

        int projectComplexity = 0;
        int maxLengthStr = 0;
        String maxString = "";
        int maxLengthComplexity = 0;

        //PRE-PROCESS//
        for (Pair<String,Integer> pair : cyclomaticComplexity) { // calculates the complexity of the project and the maximum length of a package's string for a better display
            if (pair.a.length() > maxString.length()) maxString = pair.a;
            if (pair.b > maxLengthComplexity) maxLengthComplexity = pair.b;
            projectComplexity += pair.b;
        }
        String[] p = maxString.split("\\.");
        String tmpStr = p[p.length-1];
        maxLengthStr = maxString.replace("."+tmpStr, "").length()+12;

        //PROCESS//
        String packageName = "";
        int complexity = 0;
        HashSet<String> packageVisited = new HashSet<>();
        for (Pair<String,Integer> pair : cyclomaticComplexity) {
            StringBuffer res = new StringBuffer();
            String[] parts = pair.a.split("\\."); //extract package name
            String tmpPackage = parts[parts.length-1];
            tmpPackage = pair.a.replace("."+tmpPackage, "");
            complexity+=pair.b;
            if (!tmpPackage.equals(packageName) && !packageVisited.contains(tmpPackage)) {
                packageName = tmpPackage; 
                packageVisited.add(tmpPackage);

                //display//
                res.append(packageName);
                int dif = maxLengthStr - packageName.length();
                for (int i=0; i<dif; ++i)
                    res.append(" ");
                res.append("| ");
                float complexityPourcent = ((float)complexity/(float)projectComplexity)*100;
                for (int i=0; i<complexityPourcent; ++i)
                    res.append("▄");
                res.append(" ");
                res.append(complexityPourcent);
                res.append("%");
                System.out.println(res.toString());
                complexity = 0;
            }
        }
    }
}