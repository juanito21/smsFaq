package functions.singhal;

import static functions.CommonFunctions.*;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import db.Database;
import functions.ListFunctions;

public class SinghalListFunctions extends ListFunctions {

	private List<String> stopWords;
	
	public SinghalListFunctions(String mySMS, Database myDB, int l) {
		super(mySMS, myDB, l);
		wf = new SinghalWeightFunctions(myDB);
		// PreProcessing...
		stopWords = dqh.getStopWords();
		sms = deleteWordsSinghal(sms, stopWords);
		sms = deleteSingleChar(sms);
		sms = sms.trim();
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
			if(t.length()>=s.length()) {
				List<Double> L = new ArrayList<Double>(3);
				L.add((double)Math.round(wf.weight(t,s) * 1000) / 1000);
				L.add((double)Math.round((double) csr(s,t) * 1000) / 1000);
				L.add((double)Math.round((double) truncRatio(s,t) * 1000) / 1000);
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
