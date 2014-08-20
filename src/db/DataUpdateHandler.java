package db;

import java.util.LinkedHashMap;

import static db.Database.*;

public class DataUpdateHandler extends DataHandler {

	public DataUpdateHandler(Database myDB) {
		super(myDB);
	}
	
	public int addQuestion(String q, int src) {
		LinkedHashMap<String, String> data = new LinkedHashMap<String,String>();
		data.put(Q_QUESTION, q);
		data.put(Q_IDSOURCE, ((Integer)src).toString());
		return super.addSomething(T_QUESTIONS, data, "question");
	}
	
	public int addQuestion(String q) {
		LinkedHashMap<String, String> data = new LinkedHashMap<String,String>();
		data.put(Q_QUESTION, q);
		return super.addSomething(T_QUESTIONS, data, "question");
	}
	
	public int addTag(String t) {
		LinkedHashMap<String, String> data = new LinkedHashMap<String,String>();
		data.put(T_TAG, t);
		return super.addSomething(T_TAGS, data, "tag");
	}
	
	public int addDomain(String q) {
		LinkedHashMap<String, String> data = new LinkedHashMap<String,String>();
		data.put(D_NAME, q);
		return super.addSomething(T_DOMAINS, data, "domain");
	}
	
	public int addAnswer(String a) {
		LinkedHashMap<String, String> data = new LinkedHashMap<String,String>();
		data.put(A_ANSWER, a);
		return super.addSomething(T_ANSWERS, data, "answer");
	}
	
	public int addTerm(String t) {
		LinkedHashMap<String, String> data = new LinkedHashMap<String,String>();
		data.put(T_TERM, t);
		return super.addSomething(T_TERMS, data, "term");
	}
	
	public int addSource(String s) {
		LinkedHashMap<String, String> data = new LinkedHashMap<String,String>();
		data.put(S_NAME, s);
		return super.addSomething(T_SOURCES, data, "source");
	}
	
	public int addStopWord(String w) {
		LinkedHashMap<String, String> data = new LinkedHashMap<String,String>();
		data.put(SW_WORD, w);
		return super.addSomething(T_STOPWORDS, data, "word");
	}
	
	public int addDictionaryTerm(String dt) {
		LinkedHashMap<String, String> data = new LinkedHashMap<String,String>();
		data.put(D_TERM, dt);
		return super.addSomething(T_DICT, data, "dictionaryTerm");
	}
	
	public int addQuestionTerm(int q, int t) {
		LinkedHashMap<String, Integer> data = new LinkedHashMap<String,Integer>();
		data.put(QT_IDQUESTION, (Integer)q);
		data.put(QT_IDTERM, (Integer)t);
		return super.addSomething(T_QUESTIONSTERMS, data, "questionsTerms");
	}
	
	public int addQuestionAnswer(int q, int a) {
		LinkedHashMap<String, Integer> data = new LinkedHashMap<String,Integer>();
		data.put(QA_IDQUESTION, (Integer)q);
		data.put(QA_IDANSWER, (Integer)a);
		return super.addSomething(T_QUESTIONSANSWERS, data, "questionsAnswers");
	}
	
	public int addTagDomain(int t, int d) {
		LinkedHashMap<String, Integer> data = new LinkedHashMap<String,Integer>();
		data.put(TD_IDTAG, (Integer)t);
		data.put(TD_IDDOMAIN, (Integer)d);
		return super.addSomething(T_TAGSDOMAINS, data, "tagsDomains");
	}
	
	public int addQuestionTag(int q, int t) {
		LinkedHashMap<String, Integer> data = new LinkedHashMap<String,Integer>();
		data.put(QT_IDQUESTION, (Integer)q);
		data.put(QT_IDTAG, (Integer)t);
		return super.addSomething(T_QUESTIONSTAGS, data, "questionsTags");
	}
	
	public void updateTermIdf(String term, double idf) {
		super.updateSomething(T_TERMS, T_TERM, T_IDF, term, ((Double)idf).toString());
	}

}
