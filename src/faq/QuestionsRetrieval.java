package faq;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class QuestionsRetrieval {

	public static String DICT_PATH = "dictionary/";
	
	public static void retrieveQuestionsInCSVFile(String input, String output) throws IOException {
	
		FileWriter fw = new FileWriter(output);
        PrintWriter pw = new PrintWriter(fw);
		
		BufferedReader reader = new BufferedReader(new FileReader(input));
		String line = null;
		while ((line = reader.readLine()) != null) {
			line = line.toLowerCase();
			char in[] = line.toCharArray();
			String csvLine = "";
			boolean prevCharIsSpecial = false;
			for(int i=0;i<in.length;i++) {
				if(Character.isLetter(in[i])) {
					csvLine += in[i];
					if(prevCharIsSpecial) prevCharIsSpecial = false;
				}
				else if(!prevCharIsSpecial) {
					csvLine += ";";
					prevCharIsSpecial = true;
				}
			}
			pw.println(csvLine.substring(0, csvLine.length()-1));
		}
		pw.flush();
		pw.close();
		fw.close();
	}
	
	public static void constructDictionaryFromQuestions(String input, String output) throws IOException {
		FileWriter fw = new FileWriter(output);
        PrintWriter pw = new PrintWriter(fw);
		CSVReader reader = new CSVReader(new FileReader(input), ';');
		List<String[]> qList = new ArrayList<String[]>();
		qList = reader.readAll();
		for(int i=0;i<qList.size();i++) {
			for(int j=0;j<qList.get(i).length;j++) {
				pw.print(qList.get(i)[j]);
				pw.print(";");
				pw.println(i);
			}
		}
		pw.flush();
		pw.close();
		fw.close();
	}
	
}
