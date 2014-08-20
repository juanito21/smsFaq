package functions.shivhre;

import static functions.CommonFunctions.deleteWords;
import static functions.CommonFunctions.disemvoweled;
import static functions.CommonFunctions.nbOfWordsMatching;
import static functions.CommonFunctions.toTabTerm;

import java.util.List;

import db.DataQueryHandler;
import db.Database;
import functions.ScoreFunctions;

public class ShivhreScoreFunctions extends ScoreFunctions {
	
	private DataQueryHandler dqh;
	private String smsBis;

	public ShivhreScoreFunctions(Database myDB) {
		super(myDB);
		wf = new ShivhreWeightFunctions(myDB);
		dqh = new DataQueryHandler(myDB);
		List<String> stopWords = dqh.getStopWords();
		smsBis = disemvoweled(sms);
		smsBis = deleteWords(sms, stopWords);
	}
	
	public double similarityScore() {
		double max = 0; double current; double res = 0;
		String[] sTerms = toTabTerm(sms);
		String[] qTerms = toTabTerm(question);
		for(String s : sTerms) {
			max = 0; current = 0;
			for(String q : qTerms) {
				current = wf.weight(q,s);
				if(max<current) max = current;
			} res += max;
		} return res;
	}
	
	public double keywordScore() {
		return nbOfWordsMatching(question,smsBis)/(toTabTerm(smsBis).length);
	}
	
	public double score() {
		return similarityScore() + keywordScore();
	}
	
}
