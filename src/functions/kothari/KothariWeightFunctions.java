package functions.kothari;

import static functions.CommonFunctions.*;
import db.Database;
import functions.WeightFunctions;

public class KothariWeightFunctions extends WeightFunctions {

	public KothariWeightFunctions(Database myDB) {
		super(myDB);
	}
	
	/**
	 * Compute the similarity measure between two string, that is how much two string are close
	 * @param t is the first string
	 * @param s is the second string
	 * @return a double meaning how these two strings are close
	 */
	public static double similarityMeasure(String t, String s) {
		if(t.length() != 0 && s.length() != 0) {
			if(t.charAt(0) == s.charAt(0)) {
				return lcsRatio(t,s)/(double)editDistance(t,s);
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
