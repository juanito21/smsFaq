package main;
import static functions.CommonFunctions.log;
import algorithms.ShivhreAlgo;
import db.Database;
import functions.shivhre.ShivhreWeightFunctions;

public class Main {
	
	public static String TAG = "Main";
	
	public static void main(String args[]) {
		
		long startTime = System.currentTimeMillis();
		
		Database db = Database.getInstance();
		//KothariListFunctions lf = new KothariListFunctions("psswd govrnmt vulcn bst wiz lke yu	", db, 5);
		//constructCSVFromList(lf.constructLists(), "output.csv");
		
		//DataRetrieval dr = new DataRetrieval(db);
		//dr.retrieveQuestionsInDB("questions.txt");
		
		//ShivhreAlgo na = new ShivhreAlgo("wht dctrs treat acute", db, 5);
		//String bq = na.getTheBestQuestion();
		//log(TAG, "The best question is : " + bq);
		
		//QuestionsRetrievalFromWEB qr = new QuestionsRetrievalFromWEB(db);
		//qr.retrieve();
		
		//DataRetrieval dr = new DataRetrieval(db);
		//dr.retrieveTermsFromQuestionsInDB();
		
		ShivhreWeightFunctions kwf = new ShivhreWeightFunctions(db);
		double idf = kwf.idf("zeazezefdsf");
		double res = kwf.smRatio("Pennsylvania", "Pencilvaneya");
		log(TAG, res + "");
		
		db.disconnect();
		
		long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    
	    System.out.println("Executed in " + (elapsedTime / 1000.0) + " sec");
	}

}
