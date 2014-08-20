package csv;

import static functions.CommonFunctions.log;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class ConstructOutputCSV {
	
	private static String TAG = "ConstructOutputCSV";
	private static String SEP = ";";
	
	private List<Map.Entry<String, HashMap<String,TreeSet<Map.Entry<String,List<Double>>>>>> L;
	private String output;
	private int limit;
	private String[] tokens;
	private ArrayList<String> lines;
	
	public ConstructOutputCSV(int limit, String[] tokens, List<Map.Entry<String, HashMap<String,TreeSet<Map.Entry<String,List<Double>>>>>> L, String output) {
		this.L = L;
		this.output = output;
		this.limit = limit;
		this.tokens = tokens;
	}
	
	/**
	 * Init the CSV file before fill it
	 * @param output : the file to create
	 * @param fw : the FileWriter
	 * @return a PrintWriter object
	 */
	public PrintWriter initCSV(FileWriter fw) {
		PrintWriter pw = null;
		pw = new PrintWriter(fw);
		pw.print("N° Token;N° Match;SMS Token;");
		for(int i=0;i<L.size();i++) {
			String algo = L.get(i).getKey().toLowerCase();
			switch(algo) {
				case "kothari": pw.print(algo + ";Weight;LCSRatio;EditDistance;idf; ;");
								break;
				case "shivhre": pw.print(algo + ";Weight;LCSRatio;SmRatio;idf; ;");
								break;
				case "singhal": pw.print(algo + ";Weight;CSR;TruncRatio; ;");
								break;
				case "proposed": pw.print(algo + ";Weight;LCSRatio;EditDistance2;PreCharMatchM;idf; ;");
								break;
			}
		}
		pw.println("");
		return pw;
	}
	
	public void convertList() {
		lines = new ArrayList<String>();
		String line = "";
		for(int a=0;a<tokens.length;a++) {
			for(int b=0;b<limit;b++) {
				line = "";
				line += a + SEP + b + SEP + tokens[a] + SEP;
				for(int c=0;c<L.size();c++) {
					TreeSet<Map.Entry<String,List<Double>>> t = L.get(c).getValue().get(tokens[a]);
					if(t == null) {
						log(TAG, "Stop word !");
						line += "stop word !" + SEP + SEP + SEP + SEP + SEP;
					} else {
						Map.Entry<String, List<Double>> e = t.pollFirst();
						if(e != null) {
							String term = e.getKey();
							line += term + SEP;
							for(int d=0;d<e.getValue().size();d++) {
								double value = (double)Math.round(e.getValue().get(d) * 1000) / 1000;
								line += value + SEP;
							}
							line += " " + SEP;
						}
					}
				} lines.add(line);
			} lines.add("");
		}
		
	}
	
	/**
	 * Construct a CSV file from a lists
	 * @param L : The structure
	 * @param output : the CSV file to fill up
	 */
	public void constructCSVFromList() {
		convertList();
		FileWriter fw;
		try {
			fw = new FileWriter(output);
			PrintWriter pw = initCSV(fw);
			for(String s : lines) {
				pw.println(s);
			}
			pw.flush();
			pw.close();
			fw.close();
		} catch (IOException e1) {
			log(TAG, "An error was detected while creating csv output file");
			e1.printStackTrace();
		}
	}
	
	public String csvToString() {
		String res = "";
		for(String s : lines) {
			res += s + "\n";
		} return res;
	}
	
}
