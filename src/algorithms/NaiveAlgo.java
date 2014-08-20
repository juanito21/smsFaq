package algorithms;

import static functions.CommonFunctions.log;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import db.DataQueryHandler;
import db.Database;
import functions.ListFunctions;
import functions.kothari.KothariListFunctions;
import functions.kothari.KothariScoreFunctions;
import functions.shivhre.ShivhreListFunctions;
import functions.shivhre.ShivhreScoreFunctions;

public class NaiveAlgo extends Algo {
	
	private ListFunctions lf;
	private DataQueryHandler dqh;
	private KothariScoreFunctions sf;
	private List<String> C;
	private HashMap<String, TreeSet<Map.Entry<String,List<Double>>>> L;
	private static String TAG = "NaiveAlgo";
	
	public NaiveAlgo(String sms, Database db, int limitQuestions, int limitTerms) {
		super(sms, db, limitQuestions, limitTerms);
		this.sf = new KothariScoreFunctions(db);
		this.lf = new KothariListFunctions(sms, db, limitTerms);
		sf.setSms(sms);
	}
	
	/**
	 * The comparator class
	 * @author Jean
	 *
	 */
	public class TupleComparator implements Comparator<Map.Entry<String,Double>> {
		@Override
		public int compare(Entry<String, Double> arg0, Entry<String, Double> arg1) {
			if(arg0.getValue() > arg1.getValue()) return -1;
			else return 1;
		}
	}
	
	public String getTheBestQuestion() {
		log(TAG, "Best question is finding...");
		L = lf.constructLists();
		C = lf.getCollection(L);
		toStringMetrics();
		TreeSet<Map.Entry<String, Double>> Cres = new TreeSet<Map.Entry<String, Double>>(new TupleComparator());
		double score;
		for(String q : C) {
			sf.setQuestion(q);
			score = sf.score();
			AbstractMap.SimpleEntry<String, Double> tuple =
					new AbstractMap.SimpleEntry<String, Double>(q, score);
			Cres.add(tuple);
		}
		String res = Cres.first().getKey();
		for(int i=0;i<lQ;i++) {
			Map.Entry<String, Double> tuple = Cres.pollFirst();
			if(tuple != null) {
				log(TAG, i + " : " + tuple.getKey() + " -> " + tuple.getValue());
			}
		}
		return res;
	}

	@Override
	public void toStringMetrics() {
		// TODO Auto-generated method stub
		for(Map.Entry<String, TreeSet<Map.Entry<String,List<Double>>>> e : L.entrySet()) {
			log(TAG, e.getKey());
			Iterator<Entry<String, List<Double>>> itr = e.getValue().iterator();
			while(itr.hasNext()) {
				Map.Entry<String, List<Double>> metrics = itr.next();
				log(TAG, metrics.getKey() + " -> w = " + metrics.getValue().get(0)
										  + " ; lcsRatio = " + metrics.getValue().get(1)
										  + " ; editDistance = " + metrics.getValue().get(2)
										  + " ; idf = " + metrics.getValue().get(3));
			}
		}
	}

	@Override
	public void toStringScore(Entry<String, Double> e) {
		// TODO Auto-generated method stub
		
	}

}
