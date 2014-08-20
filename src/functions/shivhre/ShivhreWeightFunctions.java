package functions.shivhre;

import static functions.CommonFunctions.*;
import db.Database;
import functions.WeightFunctions;

/**
 * The weight functions according to the Shivhre method
 * @author Jean
 *
 */
public class ShivhreWeightFunctions extends WeightFunctions {
	
	public ShivhreWeightFunctions(Database myDB) {
		super(myDB);
	}
	
	/**
	 * Compute the weight function according to Shivhre method
	 * @param w : the word
	 * @param s : the sms term
	 * @return the weight
	 */
	public double weight(String w, String s) {
		double lcsratio = lcsRatio(w,s);
		double smratio = smRatio(w,s);
		double ld = levenshteinDistance(disemvoweled(w),disemvoweled(s)) + 1;
		double res = (lcsratio*smratio*getIdf(w))/ld;
		if(res>10) {
			System.out.println("infinite !");// do something;..
		}
		return res;
	}
	
	/**
	 * Compute the LCS Ratio
	 * @param w : the first string
	 * @param s : the second string
	 * @return the LCS Ratio
	 */
	public double lcsRatio(String w, String s) {
		return (double)lcs(w,s)/max(w.length(),s.length());
	}
}
