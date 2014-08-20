package algorithms;

import static functions.CommonFunctions.log;
import static functions.CommonFunctions.preCharMatchM;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.Map.Entry;

import db.DataQueryHandler;
import db.Database;
import functions.shivhre.ShivhreListFunctions;
import functions.shivhre.ShivhreScoreFunctions;

public class ShivhreAlgo extends Algo {
	
	private ShivhreListFunctions lf;
	private ShivhreScoreFunctions sf;
	private List<Map.Entry<String, String>> C;
	private HashMap<String, TreeSet<Map.Entry<String,List<Double>>>> L;
	private static String TAG = "ShivhreAlgo";
	
	public ShivhreAlgo(String sms, Database db, int limitQuestions, int limitTerms) {
		super(sms, db, limitQuestions, limitTerms);
		this.sf = new ShivhreScoreFunctions(db);
		this.lf = new ShivhreListFunctions(sms, db, limitTerms);
		sf.setSms(sms);
	}
	
	public String getTheBestQuestion() {
		log(TAG, "Best question is finding...");
		L = lf.constructLists();
		toStringMetrics();
		C = lf.getTupleCollection(L);
		TreeSet<Map.Entry<String, Double>> Cres = new TreeSet<Map.Entry<String, Double>>(new TupleComparator());
		for(Map.Entry<String, String> qTuple : C) {
			sf.setQuestion(qTuple.getValue());
			AbstractMap.SimpleEntry<String, Double> tuple =
					new AbstractMap.SimpleEntry<String, Double>(qTuple.getKey(),
							(double)Math.round(sf.score() * 1000) / 1000);
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
										  + " ; smRatio = " + metrics.getValue().get(2)
										  + " ; idf = " + metrics.getValue().get(3));
			}
		}
	}

	@Override
	public void toStringScore(Entry<String, Double> e) {
		// TODO Auto-generated method stub
		log(TAG, e.getKey() + " -> " + e.getValue());
	}

}
