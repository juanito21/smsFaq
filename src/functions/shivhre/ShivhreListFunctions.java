package functions.shivhre;

import static functions.CommonFunctions.*;
import static functions.CommonFunctions.disemvoweled;
import static functions.CommonFunctions.lcsRatio;
import static functions.CommonFunctions.smRatio;
import static functions.CommonFunctions.toTabTerm;

import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import db.Database;
import functions.ListFunctions;

public class ShivhreListFunctions extends ListFunctions {
	
	private List<String> stopWords;
	
	public ShivhreListFunctions(String mySMS, Database myDB, int l) {
		super(mySMS, myDB, l);
		wf = new ShivhreWeightFunctions(myDB);
		stopWords = dqh.getStopWords();
		sms = sms.trim();
	}
	
	/**
	 * Get the list weight ordered from a sms term
	 * @param s
	 * @return
	 */
	public TreeSet<Map.Entry<String,List<Double>>> getListWeightedOrdered(String s) {
		TreeSet<Map.Entry<String,List<Double>>> sortedSet = new TreeSet<Map.Entry<String,List<Double>>>(new TermComparator());
		TreeSet<Map.Entry<String,List<Double>>> sortedSetLimited = new TreeSet<Map.Entry<String,List<Double>>>(new TermComparator());
		List<String> terms = dqh.getListFromSmsTerm(s);
		for(String t : terms) {
			List<Double> L = new ArrayList<Double>(3);
			L.add((double)Math.round(wf.weight(t,s) * 1000) / 1000);
			L.add((double)Math.round(lcsRatio(t,s) * 1000) / 1000);
			L.add((double)Math.round(smRatio(t,s) * 1000) / 1000);
			L.add((double)Math.round(wf.getIdf(t) * 1000) / 1000);
			Entry<String, List<Double>> e = new AbstractMap.SimpleEntry<String,List<Double>>(t, L);
			sortedSet.add(e);
		}
		if(sortedSet.size()<limit) limit = sortedSet.size();
		for(int i=0;i<limit;i++) sortedSetLimited.add(sortedSet.pollFirst());
		return sortedSetLimited;
	}

	public List<Map.Entry<String, String>> getTupleCollection(HashMap<String, TreeSet<Map.Entry<String,List<Double>>>> L) {
		List<String> C = super.getCollection(L);
		List<Map.Entry<String, String>> Cres = new ArrayList<Map.Entry<String, String>>();
		for(String q : C) {
			String qUpdate = q;
			q = disemvoweled(qUpdate);
			//q = deleteWords(qUpdate, stopWords);
			AbstractMap.SimpleEntry<String, String> tuple =
					new AbstractMap.SimpleEntry<String, String>(qUpdate, q);
			Cres.add(tuple);
		} return Cres;
	}
}
