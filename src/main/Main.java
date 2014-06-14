package main;
import static faq.QuestionsRetrieval.constructDictionaryFromQuestions;
import static faq.QuestionsRetrieval.retrieveQuestionsInCSVFile;
import static functions.WeightFunction.similarityMeasure;

import java.io.IOException;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

import db.Database;


public class Main {
	
	public static void main(String args[]) {
		
		
		try {
			retrieveQuestionsInCSVFile("questions.txt", "questions.csv");
			constructDictionaryFromQuestions("questions.csv", "terms.csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Database db = new Database();
			db.execQuery( "INSERT INTO questions (question) "
		            + "values ('Can I Get the Auto Increment Field?')");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String s = "gud";
		String t = "guided";
		
		double r = similarityMeasure(t,s);
		
		System.out.println(s + " AND " + t);
		System.out.println("output : " + r);
	}

}
