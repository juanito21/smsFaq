package functions.singhal;

import db.Database;
import functions.WeightFunctions;

import static functions.CommonFunctions.*;

/**
 * The weight functions according to the Shivhre method
 * @author Jean
 *
 */
public class SinghalWeightFunctions extends WeightFunctions {
	
	public SinghalWeightFunctions(Database myDB) {
		super(myDB);
	}
	

	public double weight(String t, String s) {
		double ui = csr(s, t) + truncRatio(s, t);
		int ld = levenshteinDistance(s,t);
		double er;
		if(ld == 0) er = 0.0;
		else er = 1.0/(levenshteinDistance(s, t));
		double a = 0.5;
		double res = a*ui + (1-a)*er;
		return res;
	}
	
}
