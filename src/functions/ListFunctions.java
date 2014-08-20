package functions;

import static functions.CommonFunctions.log;
import static functions.CommonFunctions.toTabTerm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import db.DataQueryHandler;
import db.Database;

public abstract class ListFunctions {

	protected String sms;
	protected DataQueryHandler dqh;
	protected WeightFunctions wf;
	protected int limit;
	
	protected static String TAG = "ListFunctions";
	
	public ListFunctions(String mySMS, Database myDB, int l) {
		sms = mySMS;
		dqh = new DataQueryHandler(myDB);
		limit = l;
	}
	
	/**
	 * Construct list from a SMS for each terms
	 * @param sms : the sms String
	 * @return a List
	 * @throws SQLException 
	 */
	public HashMap<String, TreeSet<Map.Entry<String,List<Double>>>> constructLists() {
		HashMap<String, TreeSet<Map.Entry<String,List<Double>>>> L = new HashMap<String,TreeSet<Map.Entry<String,List<Double>>>>();
		List<String> tabTerms = constructTabtermsFromSMS();
		for(int i=0;i<tabTerms.size();i++) L.put(tabTerms.get(i), getListWeightedOrdered(tabTerms.get(i)));
		return L;
	}
	
	public abstract TreeSet<Map.Entry<String,List<Double>>> getListWeightedOrdered(String s);
	
	/**
	 * The comparator class for the weight ordered List
	 * @author Jean
	 *
	 */
	public class TermComparator implements Comparator<Map.Entry<String,List<Double>>> {
		@Override
		public int compare(Entry<String, List<Double>> arg0, Entry<String, List<Double>> arg1) {
			if(arg0.getValue().get(0) > arg1.getValue().get(0)) return -1;
			else return 1;
		}
	}
	
	/**
	 * Construct List of terms from sms
	 * @return
	 */
	public List<String> constructTabtermsFromSMS() {
		List<String> L = new ArrayList<String>();
		String[] res = toTabTerm(sms);
		for(String s : res) L.add(s);
		return L;
	}
	
	/**
	 * Get the collection of questions
	 * @return
	 */
	public List<String> getCollection(HashMap<String, TreeSet<Map.Entry<String,List<Double>>>> L) {
		List<String> C = new ArrayList<String>();
		log(TAG, "Collection is retrieving...");
		for(Map.Entry<String,TreeSet<Map.Entry<String,List<Double>>>> e : L.entrySet()) {
			for(Map.Entry<String, List<Double>> f : e.getValue()) {
				List<String> qList = dqh.getQuestionsFromTerm(f.getKey());
				for(String s : qList) {
					if(!C.contains(s)) C.add(s);
				}
			}
		}
		log(TAG, "Collection retrieved !");
		return C;
	}
	
	
	public String getSms() {
		return sms;
	}

	public void setSms(String sms) {
		this.sms = sms;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
}
