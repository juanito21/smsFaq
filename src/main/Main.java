package main;
import static functions.WeightFunction.similarityMeasure;

import java.io.IOException;
import java.sql.SQLException;

import db.DataHandler;
import db.Database;
import faq.QuestionsRetrieval;


public class Main {
	
	public static void main(String args[]) {
		
		long startTime = System.currentTimeMillis();
		
		try {
			Database db = Database.getInstance();
			QuestionsRetrieval qr = new QuestionsRetrieval(db);
			qr.retrieveQuestionsInDB("questions.txt");
			db.disconnect();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    
	    System.out.println("Executed in " + elapsedTime / 1000 + " sec");
	}

}
