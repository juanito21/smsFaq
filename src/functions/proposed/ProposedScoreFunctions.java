package functions.proposed;

import java.util.HashMap;

import static functions.CommonFunctions.*;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import db.Database;
import functions.ScoreFunctions;

public class ProposedScoreFunctions extends ScoreFunctions {
	
	private HashMap<String, TreeSet<Map.Entry<String,List<Double>>>> L;

	public ProposedScoreFunctions(Database myDB, HashMap<String, TreeSet<Map.Entry<String,List<Double>>>> L) {
		super(myDB);
		wf = new ProposedWeightFunctions(myDB);
		this.L = L;
	}
	
	public double score() {
		int qScore = 0;
		for(Map.Entry<String, TreeSet<Map.Entry<String,List<Double>>>> l : L.entrySet()) {
			TreeSet<Map.Entry<String,List<Double>>> currentList = l.getValue();
			for(Map.Entry<String,List<Double>> e : currentList) {
				if(nbOfWordsMatching(e.getKey(), question)>0) qScore++;
			}
		}
		return qScore;
	}
	
}
