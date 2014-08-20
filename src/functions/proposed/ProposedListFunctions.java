package functions.proposed;

import static functions.CommonFunctions.*;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import db.Database;
import functions.ListFunctions;

public class ProposedListFunctions extends ListFunctions {
	
	public ProposedListFunctions(String mySMS, Database myDB, int l) {
		super(mySMS, myDB, l);
		wf = new ProposedWeightFunctions(myDB);
	}
	
	/**
	 * Get the list weight ordered from a sms term
	 * @param s
	 * @return
	 */
	public TreeSet<Map.Entry<String,List<Double>>> getListWeightedOrdered(String s) {
		log(TAG, "List retrieving...");
		TreeSet<Map.Entry<String,List<Double>>> sortedSet = new TreeSet<Map.Entry<String,List<Double>>>(new TermComparator());
		TreeSet<Map.Entry<String,List<Double>>> sortedSetLimited = new TreeSet<Map.Entry<String,List<Double>>>(new TermComparator());
		List<String> terms = dqh.getListFromSmsTerm(s);
		for(String t : terms) {
			if(lcs(t,s)>=(s.length()-2)) {
				List<Double> L = new ArrayList<Double>(3);
				L.add((double)Math.round(wf.weight(t,s) * 1000) / 1000);
				L.add((double)Math.round((double)lcsRatio(t,s) * 1000) / 1000);
				L.add((double)Math.round((double)editDistance(cs(t),s) * 1000) / 1000);
				L.add((double)Math.round((double)preCharMatchM(t,s) * 1000) / 1000);
				L.add((double)Math.round((double)wf.getIdf(t) * 1000) / 1000);
				log(TAG, t);
				Entry<String, List<Double>> e = new AbstractMap.SimpleEntry<String,List<Double>>(t, L);
				sortedSet.add(e);
			}
		}
		if(sortedSet.size()<limit) limit = sortedSet.size();
		for(int i=0;i<limit;i++) sortedSetLimited.add(sortedSet.pollFirst());
		return sortedSetLimited;
	}
	
	
}
