package algorithms;

import java.util.ArrayList;
import java.util.Map;

import db.DataQueryHandler;
import db.Database;

public abstract class Algo {
	
	protected DataQueryHandler dqh;
	protected String sms;
	protected int lT;
	protected int lQ;
	
	public Algo(String sms, Database db, int limitQuestions, int limitTerms) {
		this.dqh = new DataQueryHandler(db);
		this.sms = sms;
		this.lT = limitTerms;
		this.lQ = limitQuestions;
	}
	
	public abstract String getTheBestQuestion();
	
	public abstract void toStringMetrics();
	
	public abstract void toStringScore(Map.Entry<String, Double> e);

}
