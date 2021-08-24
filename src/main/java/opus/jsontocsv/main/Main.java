package opus.jsontocsv.main;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import opus.jsontocsv.parser.JSONFlattener;
import opus.jsontocsv.writer.CSVWriter;

public class Main {
	
	//private static String path = "C:\\Users\\raj.singh\\Downloads\\DIReport\\diReport\\performanceOutput";


    public static void main(String[] args) throws Exception {
        /*
         *  Parse a JSON String and convert it to CSV
         */
    	//String path = args[0];
    	Scanner sc = new Scanner(System.in);
    	System.out.println("Enter directory path to read files");
    	String directoryPath = sc.next();
    	System.out.println(directoryPath);
    	if(Files.isDirectory(Paths.get(directoryPath))){
    		System.out.println("folder exists");
    	} else {
    		System.out.println("does not");
    	}
    	System.out.println("Enter directory path for output files");
    	String outputDirectory = sc.next();
    	System.out.println(outputDirectory);
    	System.out.println("Enter output file name without any extensions");
    	String outputFileName = sc.next();
    	
        List<Map<String, String>> flatJson = new ArrayList<>();
        List<Path> jsonFilePaths = Files.walk(Paths.get(directoryPath))
            	.filter(Files::isRegularFile)
            	.filter(f -> f.getFileName().toString().endsWith("json"))
            	.collect(Collectors.toList());
        
        System.out.println("total json files to parse " + jsonFilePaths.size());
        
        List<Map<String, String>> finalJson = new ArrayList<>();
        Set<String> header = new TreeSet<>();
        for(Path p : jsonFilePaths) {
        	flatJson = JSONFlattener.parseJson(new File(p.toString()), "UTF-8");
        	flatJson.get(0).put("fullFilePath",p.toString());
            header = CSVWriter.collectOrderedHeaders(flatJson);
            // the intention is generate a csv file with specific headers - not all
            finalJson.add(flatJson.get(0));
            
        }
        
        String outputFileNamePath = outputDirectory + "/output/" + outputFileName + ".csv";
        CSVWriter.writeLargeFile(finalJson, ";", outputFileNamePath, header);
        System.out.println("output file path : " + outputFileNamePath);
          
    }

    private static String jsonString() {
        return  "[" +
                "    {" +
                "        \"studentName\": \"Foo\"," +
                "        \"Age\": \"12\"," +
                "        \"subjects\": [" +
                "            {" +
                "                \"name\": \"English\"," +
                "                \"marks\": \"40\"" +
                "            }," +
                "            {" +
                "                \"name\": \"History\"," +
                "                \"marks\": \"50\"" +
                "            }" +
                "        ]" +
                "    }" +
                "]";
    }
}

