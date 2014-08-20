package functions;

import db.Database;

public abstract class ScoreFunctions {
	
	protected String sms;
	protected String question;
	protected WeightFunctions wf;
	
	public ScoreFunctions(Database myDB) {
		sms = "";
		question = "";
	}

	public abstract double score();

	public String getSms() {
		return sms;
	}

	public void setSms(String sms) {
		this.sms = sms;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}
}
