package faq;

import static functions.CommonFunctions.SEP;
import static functions.CommonFunctions.log;
import static functions.CommonFunctions.toFormatString;
import static functions.CommonFunctions.toTabTerm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;
import db.DataQueryHandler;
import db.DataUpdateHandler;
import db.Database;

/**
 * QuestionRetrieval retrieves all questions from a .txt in the database
 * @author Jean
 *
 */
public class DataRetrieval {

	public static String DICT_PATH = "dictionary/";
	public static String TAG = "QuestionRetrieval";
	private DataUpdateHandler duh; 
	private DataQueryHandler dqh;
	
	/**
	 * The constructor
	 * @param myDB : Need a database for recording questions, terms, answers,..
	 * @throws SQLException
	 */
	public DataRetrieval(Database myDB) {
		duh = new DataUpdateHandler(myDB);
		dqh = new DataQueryHandler(myDB);
	}
	
	public void retrieveQuestionsInDB(String input, String dom, String[] tags, String src) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(input));
			String line = null;
			String qLine = null;
			boolean q = false;
			int idQ = 0, idT = 0, idA = 0, idD = 0, idS = 0;
			List<Integer> idTags = new ArrayList<Integer>();
			log(TAG, "Source retrieving...");
			if(!dqh.isSourceExist(src)) idS = duh.addSource(src);
			else idS = dqh.getIdSource(src);
			log(TAG, "Domain retrieving...");
			if(!dqh.isDomainExist(dom)) idD = duh.addDomain(dom);
			else idD = dqh.getIdDomain(dom);
			log(TAG, "Tags retrieving...");
			for(int i=0;i<tags.length;i++) {
				if(!dqh.isTagExist(tags[i])) {
					idTags.add(duh.addTag(tags[i]));
					duh.addTagDomain(idTags.get(idTags.size()-1), idD);
				}
				else idTags.add(dqh.getIdTag(tags[i]));
			}
			log(TAG, "Questions retrieving...");
			while ((line = reader.readLine()) != null) {
				line = line.toLowerCase().trim();
				if(line.length()>0) {
					if(line.charAt(0) == '*') {
						q = true; // It means it's a question !
						qLine = line.substring(1);
					}
					else q = false;
					if(q && !dqh.isQuestionExists(qLine)) { // we have to add the question
						idQ = duh.addQuestion(qLine, idS);
						log(TAG, "Question n°" + idQ + " added !");
						for(int i=0;i<idTags.size();i++) {
							duh.addQuestionTag(idQ, idTags.get(i));
						}
						String[] terms = toTabTerm(qLine);
						for(String term : terms) {
							if(!dqh.isTermExists(term)) {
								idT = duh.addTerm(term);
								log(TAG, "Term n°" + idT + " added !");
								duh.addQuestionTerm(idQ, idT);
							}
						}
					} else if(!q && !dqh.isAnswerExists(line)) { // answer
						if(idQ == 0) idQ = dqh.getIdQuestion(line);
						idA = duh.addAnswer(line);
						log(TAG, "Answer n°" + idA + " added !");
						duh.addQuestionAnswer(idQ, idA);
					}
				}
			}
			duh.close();
			dqh.close();
			log(TAG, "Questions retrieved !");
		} catch (IOException e) {
			log(TAG, "An error was detected while retrieving questions in DB from .txt file");
			e.printStackTrace();
		}
	}
	
	/**
	 * Compute the inverse document frequency for a term
	 * @param t is the term
	 * @return the idf of the term
	 */
	public double idf(String t, int N) {
		int f = dqh.getNumberOfQuestionWithTerm(t) + 1;
		double res = Math.log(N/f);
		return res;
		//return 1.0;
	}
	
	public void updateAllIdfTermInDB() {
		List<String> tList = dqh.getAllTerms();
		int N = dqh.getNumberOfQuestion();
		double idf;
		for(String s : tList) {
			idf = idf(s, N);
			log(TAG, "UPDATE : " + s + " with idf of " + idf);
			duh.updateTermIdf(s, idf);
		}
	}
	
	/**
	 * Retrieve questions from .txt in a database
	 * @param input : .txt source file
	 * @throws IOException
	 * @throws SQLException
	 */
	public void retrieveQuestionsInDB(String input) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(input));
			String line = null;
			int i = 0;
			int idQ = 0, idT = 0, idA = 0;
			log(TAG, "Questions retrieving...");
			while ((line = reader.readLine()) != null) {
				line = line.toLowerCase().trim();
				if(i%2 == 0 && !dqh.isQuestionExists(line)) { // question
					idQ = duh.addQuestion(line);
					log(TAG, "Question n°" + idQ + " added !");
					String[] terms = toTabTerm(line);
					for(String term : terms) {
						if(!dqh.isTermExists(term)) {
							idT = duh.addTerm(term);
							log(TAG, "Term n°" + idT + " added !");
							duh.addQuestionTerm(idT, idQ);
						}
					}
				} else if(i%2 == 1 && !dqh.isAnswerExists(line)) { // answer
					idA = duh.addAnswer(line);
					log(TAG, "Answer n°" + idA + " added !");
					duh.addQuestionAnswer(idQ, idA);
				}
				i++;
			}
			duh.close();
			dqh.close();
			log(TAG, "Questions retrieved !");
		} catch (IOException e) {
			log(TAG, "An error was detected while retrieving questions in DB from .txt file");
			e.printStackTrace();
		}
	}
	
	public void retrieveTermsFromQuestionsInDB() {
		log(TAG, "Retrieving terms from questions in DB...");
		List<Map.Entry<Integer, String>> Q = new ArrayList<Map.Entry<Integer,String>>();
		Q = dqh.getAllQuestions();
		for(Map.Entry<Integer,String> q : Q) {
			String[] terms = toTabTerm(q.getValue().toString());
			for(String t : terms) {
				if(!dqh.isTermExists(t)) {
					log(TAG, "Adding term : " + t);
					int idT = duh.addTerm(t);
					duh.addQuestionTerm((int)q.getKey(), idT);
				}
			}
		}
	}
	
	/**
	 * Retrieve terms from a dictionary in the database
	 * @param input : the dictionary .csv file
	 */
	public void retrieveDictionaryTermsInDB(String input) {
		log(TAG, "Dictionary terms retrieving...");
		CSVReader reader;
		try {
			reader = new CSVReader(new FileReader(input), ';');
			List<String[]> tList = new ArrayList<String[]>();
			tList = reader.readAll();
			for(int i=0;i<tList.size();i++) duh.addDictionaryTerm(tList.get(i)[0]);
		} catch (IOException e) {
			log(TAG, "An error was detected while opening csv dictionary file");
			e.printStackTrace();
		}
		log(TAG, "Dictionary terms retrieved !");
	}
	
	public void retrieveStopWordsInDB(String input) {
		log(TAG, "Stop words retrieving...");
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(input));
			String line;
			while ((line = br.readLine()) != null) {
			   duh.addStopWord(line);
			}
			br.close();
		} catch (IOException e) {
			log(TAG, "An error was detected while reading the stop words file");
			e.printStackTrace();
		}
	}

	/**
	 * OLD FUNCTION !
	 * Retrieve questions from a .txt File to a .csv file
	 * @param input : the .txt file input
	 * @param output : the csv file output
	 * @throws IOException
	 */
	public static void retrieveQuestionsInCSVFile(String input, String output) throws IOException {
	
		FileWriter fw = new FileWriter(output);
        PrintWriter pw = new PrintWriter(fw);
		
		BufferedReader reader = new BufferedReader(new FileReader(input));
		String line = null;
		while ((line = reader.readLine()) != null) {
			line = line.toLowerCase();
			line = toFormatString(line, SEP);
			pw.println(line.substring(0, line.length()-1));
		}
		pw.flush();
		pw.close();
		fw.close();
	}
	
	
	/**
	 * OLD FUNCTION !
	 * Construct the dictionnary from .csv file to a .csv file
	 * @param input .csv file with questions
	 * @param output .csv file with terms
	 * @throws IOException
	 */
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
