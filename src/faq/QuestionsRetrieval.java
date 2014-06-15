package faq;

import static functions.CommonFunctions.escapeSQL;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static functions.CommonFunctions.*;

import au.com.bytecode.opencsv.CSVReader;
import db.DataHandler;
import db.Database;

public class QuestionsRetrieval {

	public static String DICT_PATH = "dictionary/";
	public static String SEP = ";";
	public static String TAG = "QuestionRetrieval";
	public DataHandler dh;
	
	public QuestionsRetrieval(Database myDB) throws SQLException {
		dh = new DataHandler(myDB);
	}
	
	public static String toFormatString(String line, String sep) {
		line = line.toLowerCase();
		char in[] = line.toCharArray();
		String res = "";
		boolean prevCharIsSpecial = false;
		for(int i=0;i<in.length;i++) {
			if(Character.isLetter(in[i])) {
				res += in[i];
				if(prevCharIsSpecial) prevCharIsSpecial = false;
			}
			else if(!prevCharIsSpecial) {
				res += sep;
				prevCharIsSpecial = true;
			}
		}
		return res;
	}
	
	public void retrieveQuestionsInDB(String input) throws IOException, SQLException {
		BufferedReader reader = new BufferedReader(new FileReader(input));
		String line = null;
		int i = 0;
		int idQ = 0, idT = 0, idA = 0;
		log(TAG, "Questions retrieving...");
		while ((line = reader.readLine()) != null) {
			line = escapeSQL(line.toLowerCase().trim());
			if(i%2 == 0 && !dh.isQuestionExists(line)) { // question
				idQ = dh.addQuestion(line);
				log(TAG, "Question n°" + idQ + " added !");
				line = toFormatString(line, SEP);
				String[] terms = line.split(SEP);
				for(String term : terms) {
					if(!dh.isTermExists(term)) {
						idT = dh.addTerm(escapeSQL(term));
						log(TAG, "Term n°" + idT + " added !");
						dh.addQuestionTerm(idT, idQ);
					}
				}
			} else if(i%2 == 1 && !dh.isAnswerExists(line)) { // answer
				idA = dh.addAnswer(line);
				log(TAG, "Answer n°" + idA + " added !");
				dh.addQuestionAnwser(idQ, idA);
			}
			i++;
		}
		dh.close();
		log(TAG, "Questions retrieved !");
	}
	
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
