	package functions.kothari;

import static functions.CommonFunctions.toTabTerm;
import db.Database;
import functions.ScoreFunctions;

public class KothariScoreFunctions extends ScoreFunctions {

	public KothariScoreFunctions(Database myDB) {
		super(myDB);
		wf = new KothariWeightFunctions(myDB);
	}
	
	public double score() {
		double max = 0; double current; double res = 0;
		String[] sTerms = toTabTerm(sms);
		String[] qTerms = toTabTerm(question);
		for(String s : sTerms) {
			max = 0; current =0;
			for(String q : qTerms) {
				current = wf.weight(q,s);
				if(max<current) max = current;
			} res += max;
		} return res;
	}
	
}
