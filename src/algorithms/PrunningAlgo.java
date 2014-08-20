package algorithms;

import static functions.CommonFunctions.log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import db.DataQueryHandler;
import db.Database;
import functions.kothari.KothariListFunctions;
import functions.kothari.KothariScoreFunctions;

public class PrunningAlgo extends Algo {
	
	private KothariScoreFunctions sf;
	private KothariListFunctions lf;
	private HashMap<String, TreeSet<Map.Entry<String,List<Double>>>> L;
	private static String TAG = "PrunningAlgo";
	
	public PrunningAlgo(String sms, Database db, int limitQuestions, int limitTerms) {
		super(sms, db, limitQuestions, limitTerms);
		lf = new KothariListFunctions(sms, db, limitTerms);
		sf = new KothariScoreFunctions(db);
		sf.setSms(sms);
	}
	
	public String getTheBestQuestion() {
		int i = 0;
		String theBest = "";
		log(TAG, "Best question is finding...");
		HashMap<String, Double> C = new HashMap<String, Double>();
		L = lf.constructLists();
		toStringMetrics();
		log(TAG, "Lists constructed !");
		while(true) {
			Map.Entry<String, TreeSet<Map.Entry<String, List<Double>>>> lMax = null;
			Map.Entry<String, List<Double>> eCurrent = null;
			double current; double max = 0; double ub = 0;
			for(Map.Entry<String, TreeSet<Map.Entry<String,List<Double>>>> l : L.entrySet()) {
				eCurrent = l.getValue().first();
				if(eCurrent != null) {
					current = eCurrent.getValue().get(0);
					ub += current;
					if(current>=max) {
						max = current;
						lMax = l;
					}
				}
			}
			Map.Entry<String, List<Double>> bestTerm = L.get(lMax.getKey()).first();
			ub -= L.get(lMax.getKey()).first().getValue().get(0);
			L.get(lMax.getKey()).pollFirst();	
			ub += L.get(lMax.getKey()).first().getValue().get(0);
			String term = bestTerm.getKey();
			List<String> qList = dqh.getQuestionsFromTerm(term);
			for(String q : qList) {
				sf.setQuestion(q);
				C.put(q, sf.score());
			}
			max = 0;
			Map.Entry<String, Double> qHat = null;
			for(Map.Entry<String, Double> e : C.entrySet()) {
				current = e.getValue();
				if(current>=max) {
					max = current;
					qHat = e;
				}
			}
			toStringScore(qHat);
			if(qHat.getValue()>=ub) {
				return qHat.getKey();
			}
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
										  + " ; editDistance = " + metrics.getValue().get(2));
			}
		}
	}

	@Override
	public void toStringScore(Map.Entry<String, Double> e) {
		log(TAG, e.getKey() + " -> " + e.getValue());
		
	}
}
