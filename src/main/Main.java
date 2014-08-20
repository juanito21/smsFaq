package main;
import static functions.CommonFunctions.log;

import java.util.ArrayList;

import algorithms.NaiveAlgo;
import algorithms.ShivhreAlgo;
import db.Database;

public class Main {
	
	public static String TAG = "Main";
	
	public static void main(String args[]) {
		
		long startTime = System.currentTimeMillis();
		Database db = Database.getInstance();
		
		ArrayList<String> lSMS = new ArrayList<String>();
		lSMS.add("wht is a dbit crd");
		//lSMS.add("wht is the roi of cr loan");
		//lSMS.add("wht is personal ident numb");
		//lSMS.add("wht is autom telir machn");
		//lSMS.add("hw r the loqn limits decided");
		/*for(String sms : lSMS) {
			String[] tokens = toTabTerm(sms);
			int limit = 5;
			List<Map.Entry<String, HashMap<String,TreeSet<Map.Entry<String,List<Double>>>>>> L = new ArrayList<Map.Entry<String, HashMap<String,TreeSet<Map.Entry<String,List<Double>>>>>>();
			
			//SinghalListFunctions slf = new SinghalListFunctions(sms, db, limit);
			KothariListFunctions klf = new KothariListFunctions(sms, db, limit);
			//ProposedListFunctions plf = new ProposedListFunctions(sms, db, limit);
			ShivhreListFunctions shlf = new ShivhreListFunctions(sms, db, limit);
			
			AbstractMap.SimpleEntry<String, HashMap<String,TreeSet<Map.Entry<String,List<Double>>>>> e1;
			AbstractMap.SimpleEntry<String, HashMap<String,TreeSet<Map.Entry<String,List<Double>>>>> e2;
			AbstractMap.SimpleEntry<String, HashMap<String,TreeSet<Map.Entry<String,List<Double>>>>> e3;
			AbstractMap.SimpleEntry<String, HashMap<String,TreeSet<Map.Entry<String,List<Double>>>>> e4;
			
			//e1 = new AbstractMap.SimpleEntry<String, HashMap<String,TreeSet<Map.Entry<String,List<Double>>>>>("Singhal", slf.constructLists());
			e2 = new AbstractMap.SimpleEntry<String, HashMap<String,TreeSet<Map.Entry<String,List<Double>>>>>("Kothari", klf.constructLists());
			//e3 = new AbstractMap.SimpleEntry<String, HashMap<String,TreeSet<Map.Entry<String,List<Double>>>>>("Proposed", plf.constructLists());
			e4 = new AbstractMap.SimpleEntry<String, HashMap<String,TreeSet<Map.Entry<String,List<Double>>>>>("Shivhre", shlf.constructLists());
			
			L.add(e2);
			L.add(e4);
			//L.add(e1);
			//L.add(e3);

			ConstructOutputCSV csvBuilder = new ConstructOutputCSV(limit, tokens, L, "" + sms + ".csv");
			csvBuilder.constructCSVFromList();
		}*/
		
		
		
		/*String[] lFiles = {"Internet Crime Complaint Center"};
		DataRetrieval dr = new DataRetrieval(db);
		String src = "faq/";
		for(String t : lFiles) {
			String[] tags = {t};
			dr.retrieveQuestionsInDB(src+t+".txt", "Internet Crime Complaint Center", tags, "http://www.indiabix.com/");
		}*/
		
		//DataRetrieval dr = new DataRetrieval(db);
		//dr.retrieveQuestionsInDB("questions.txt");
		
		//DataRetrieval dr = new DataRetrieval(db);
		//dr.updateAllIdfTermInDB();
		
		//int test = editDistance("government", "govt");
		//log(TAG, test+"");
		
		for(String sms : lSMS) {
			log(TAG, "***** KOTHARI *****");
			NaiveAlgo na = new NaiveAlgo(sms, db, 5, 5);
			na.getTheBestQuestion();
			log(TAG, "***** SHIVHRE *****");
			ShivhreAlgo sa = new ShivhreAlgo(sms, db, 5, 5);
			sa.getTheBestQuestion();
		}
		/*NaiveAlgo na = new NaiveAlgo("whr my dbit crd can v usd", db, 5, 5);
		String bq = na.getTheBestQuestion();
		log(TAG, "The best question is : " + bq);*/
	
		//QuestionsRetrievalFromWEB qr = new QuestionsRetrievalFromWEB(db);
		//qr.retrieve();
		
		//DataRetrieval dr = new DataRetrieval(db);
		//dr.retrieveTermsFromQuestionsInDB();
		
		//ShivhreWeightFunctions kwf = new ShivhreWeightFunctions(db);
		//double idf = kwf.idf("zeazezefdsf");
		//double res = kwf.smRatio("Pennsylvania", "Pencilvaneya");
		//log(TAG, res + "");
		
		db.disconnect();
		
		long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    
	    System.out.println("Executed in " + (elapsedTime / 1000.0) + " sec");
	}

}
