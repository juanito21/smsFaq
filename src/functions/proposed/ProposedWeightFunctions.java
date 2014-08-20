package functions.proposed;

import static functions.CommonFunctions.*;
import db.Database;
import functions.WeightFunctions;

public class ProposedWeightFunctions extends WeightFunctions {

	public ProposedWeightFunctions(Database myDB) {
		super(myDB);
	}
	
	/**
	 * Compute the similarity measure between two string, that is how much two string are close
	 * @param t is the term
	 * @param s is the sms token
	 * @return a double meaning how these two strings are close
	 */
	public static double similarityMeasure(String t, String s) {
		if(t.length() != 0 && s.length() != 0) {
			if(t.charAt(0) == s.charAt(0)) {
				double res = lcsRatio(t,s)/(double)editDistance(cs(t), s);
				if(lcs(s,t) == s.length()) res += 1.0;
				double m = preCharMatchM(t,s);
				if(m>=0.5) res += m;
				return res;
			} else return 0.0;
		} else return 0.0;
	}
	
	/**
	 * Compute the weight of a term compare to a noisy word from a SMS
	 * @param t is the term
	 * @param s is the noisy word from a SMS
	 * @return the weight
	 */
	public double weight(String t, String s) {
		return similarityMeasure(t, s) * getIdf(t);
	}
}
