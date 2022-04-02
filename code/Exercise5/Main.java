package fr.istic.vv;

import com.github.javaparser.utils.SourceRoot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {
        if(args.length == 0) {
            System.err.println("Should provide the path to the source code");
            System.exit(1);
        }

        File file = new File(args[0]);
        if(!file.exists() || !file.isDirectory() || !file.canRead()) {
            System.err.println("Provide a path to an existing readable directory");
            System.exit(2);
        }

        
        SourceRoot root = new SourceRoot(file.toPath());
        TCC_Compute printer = new TCC_Compute();
        root.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> unit.accept(printer, null));
            return SourceRoot.Callback.Result.DONT_SAVE;
        });

   
        // fichier contenant la liste des classes avec leur TCC et package
        try {
            FileWriter fw = new FileWriter("noGetterResult.txt");
            fw.write(TCC_Compute.getResult());
            fw.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        
        // fichier contenant l'histogramme des TCC au sein du projet
        try {
            FileWriter fw = new FileWriter("Histogramme.txt");
            fw.write(createHistogramm(TCC_Compute.getMapTCC()));
            fw.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        
        
        
    }
    
    public static String createHistogramm(HashMap<String, Double> mapTCC)
    {
    	int total = 0;
    	HashMap<Double, Integer> map_histo_TCC = new HashMap <Double, Integer>();
    	for( Map.Entry<String, Double> class_row : mapTCC.entrySet())
    	{
    		double TCC_key = Math.round(class_row.getValue()*10)/10.0;
    		if ( map_histo_TCC.containsKey(TCC_key))
    		{
    			map_histo_TCC.replace(TCC_key, map_histo_TCC.get(TCC_key)+1);
    		}
    		else
    		{
    			map_histo_TCC.put(TCC_key, 1);
    		}
    		total++;
    	}
    	String result = "Histogramme des TCC des classes du projet : \n";
    	
    	for (int key = 0; key <= 10; key ++ )
    	{
    		int nbEtoiles = (int) (150*map_histo_TCC.get(key/10.0)/total);
    		result += (key/10.0) + makeChars(5, ' ') + " : " + makeChars(nbEtoiles, '*') + "\n";
    	}
		return result;
    	
    }
    
    public static String makeChars(int N, char car)
    {
    	String result = "";
    	for(int i=0; i<N; i++)
    	{
    		result += car;
    	}
    	return result;
    }


}
