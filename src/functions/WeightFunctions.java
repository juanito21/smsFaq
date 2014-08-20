package functions;

import db.DataQueryHandler;
import db.Database;

public abstract class WeightFunctions {
	
	protected DataQueryHandler dqh;
	
	public WeightFunctions(Database myDB) {
		dqh = new DataQueryHandler(myDB);
	}
	
	/**
	 * Compute the inverse document frequency for a term
	 * @param t is the term
	 * @return the idf of the term
	 */
	public double getIdf(String t) {
		//return dqh.getIdf(t);
		return 1.0;
	}
	
	public abstract double weight(String s, String t);
}
