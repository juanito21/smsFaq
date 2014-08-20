package db;

import static db.Database.*;
import static functions.CommonFunctions.log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

/**
 * Query data from the database
 * @author Jean
 *
 */
public class DataQueryHandler extends DataHandler {

	private static String TAG = "DataQueryHandler";
	
	public DataQueryHandler(Database myDB) {
		super(myDB);
	}
	
	/**
	 * Check if a question exists
	 * @param q : the question
	 * @return TRUE if it exists, FALSE else
	 */
	public boolean isQuestionExists(String q) {
		return super.isSomethingExists(T_QUESTIONS, Q_QUESTION, q, "question");
	}
	
	/**
	 * Check if an anwser exists
	 * @param a : the answer
	 * @return TRUE if it exists, FALSE else
	 */
	public boolean isAnswerExists(String a) {
		return super.isSomethingExists(T_ANSWERS, A_ANSWER, a, "answer");
	}
	
	/**
	 * Check if a term exists
	 * @param t : the term
	 * @return TRUE if it exists, FALSE else
	 */
	public boolean isTermExists(String t) {
		return super.isSomethingExists(T_TERMS, T_TERM, t, "term");
	}
	
	/**
	 * Check if a tag exists
	 * @param t : the tag
	 * @return TRUE if it exists, FALSE else
	 */
	public boolean isTagExist(String t) {
		return super.isSomethingExists(T_TAGS, T_TAG, t, "tag");
	}
	
	/**
	 * Check if a domain exists
	 * @param t : the domain
	 * @return TRUE if it exists, FALSE else
	 */
	public boolean isDomainExist(String t) {
		return super.isSomethingExists(T_DOMAINS, D_NAME, t, "domain");
	}
	
	/**
	 * Check if a source exists
	 * @param s : the source
	 * @return TRUE if it exists, FALSE else
	 */
	public boolean isSourceExist(String s) {
		return super.isSomethingExists(T_SOURCES, S_NAME, s, "source");
	}
	
	public int getIdDomain(String dom) {
		return super.getSomething(T_DOMAINS, D_IDDOMAIN, D_NAME, dom);
	}
	
	public int getIdTag(String tag) {
		return super.getSomething(T_TAGS, T_IDTAG, T_TAG, tag);
	}
	
	public int getIdQuestion(String q) {
		return super.getSomething(T_QUESTIONS, Q_ID, Q_QUESTION, q);
	}
	
	public int getIdSource(String source) {
		return super.getSomething(T_SOURCES, S_IDSOURCE, S_NAME, source);
	}
	
	/**
	 * Get a list of term from a sms term
	 * @param t : the sms term
	 * @return the list of term matching with the first letter of t and >=t.length()
	 */
	public List<String> getListFromSmsTerm(String t) {
		List<String> L = new ArrayList<String>();
		String fQ = t.substring(0,1);
		String sql = "SELECT " + D_TERM + " FROM " + T_TERMS + " WHERE " + D_TERM + " LIKE ? AND LENGTH(" + D_TERM + ")>=?;";
		try {
			preStmnt = (PreparedStatement) db.getConnection().prepareStatement(sql);
			preStmnt.setString(1,fQ+"%");
			preStmnt.setInt(2,t.length());
			rs = db.execQuery(preStmnt);
			while(rs.next()) L.add(rs.getString("term"));
		} catch (SQLException e) {
			log(TAG, "An error was detected while getting list from a sms term :" + t);
			e.printStackTrace();
		} return L;
	}
	
	/**
	 * Get all terms
	 * @return List of terms
	 */
	public List<String> getAllTerms() {
		return super.getAllSomething(T_TERMS, T_TERM);
	}
	
	/**
	 * Get all questions
	 * @return List of questions
	 */
	public List<Map.Entry<Integer,String>> getAllQuestions() {
		return super.getAllSomething(T_QUESTIONS, Q_ID, Q_QUESTION);
	}
	
	/**
	 * Get all questions containing the term
	 * @param t : the term
	 * @return List of questions containing the term
	 */
	public List<String> getQuestionsFromTerm(String t) {
		List<String> L = new ArrayList<String>();
		String sql = "SELECT " + Q_QUESTION + " FROM " + T_QUESTIONS + " WHERE " + Q_QUESTION + " REGEXP ?;";
		try {
			preStmnt = (PreparedStatement) db.getConnection().prepareStatement(sql);
			preStmnt.setString(1, "^(.*)[^a-z]*" + t + "[^a-z]*(.*)$");
			rs = db.execQuery(preStmnt);
			while(rs.next()) L.add(rs.getString(Q_QUESTION));
		} catch (SQLException e) {
			log(TAG, "An error was detected while getting questions list from a term :" + t);
			e.printStackTrace();
		} return L;
	}
	
	/**
	 * Get the stop words
	 * @return List of stop words
	 */
	public List<String> getStopWords() {
		List<String> L = new ArrayList<String>();
		String sql = "SELECT " + SW_WORD + " FROM " + T_STOPWORDS + ";";
		try {
			preStmnt = (PreparedStatement) db.getConnection().prepareStatement(sql);
			rs = db.execQuery(preStmnt);
			while(rs.next()) L.add(rs.getString(SW_WORD));
		} catch (SQLException e) {
			log(TAG, "An error was detected while getting stop words list");
			e.printStackTrace();
		} return L;
	}
	
	public int getNumberOfQuestion() {
		String sql = "SELECT COUNT(*) FROM " + T_QUESTIONS + ";";
		int nb = -1;
		try {
			preStmnt = (PreparedStatement) db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			rs = db.execQuery(preStmnt);
			if(rs.next()) nb = rs.getInt(1);
			rs.close();
		} catch (SQLException e) {
			log(TAG, "An error was detected while computing number of question.");
			e.printStackTrace();
		} return nb;
	}
	
	public int getNumberOfQuestionWithTerm(String t) {
		int nb = -1;
		String sql = "SELECT COUNT(*) FROM " + T_QUESTIONS + " WHERE " + Q_QUESTION + " REGEXP ?;";
		try {
			preStmnt = (PreparedStatement) db.getConnection().prepareStatement(sql);
			preStmnt.setString(1, "[[:<:]]" + t + "[[:>:]]");
			rs = db.execQuery(preStmnt);
			if(rs.next()) nb = rs.getInt(1);
		} catch (SQLException e) {
			log(TAG, "An error was detected while getting number of question froma term :" + t);
			e.printStackTrace();
		} return nb;
	}
	
	public double getIdf(String term) {
		double res = 1.0;
		String sql = "SELECT " + T_IDF + " FROM " + T_TERMS + " WHERE " + T_TERM + " = ?;";
		try {
			preStmnt = (PreparedStatement) db.getConnection().prepareStatement(sql);
			preStmnt.setString(1, term);
			rs = db.execQuery(preStmnt);
			if(rs.next()) res = rs.getInt(T_IDF);
		} catch (SQLException e) {
			log(TAG, "An error was detected while getting term idf :" + term);
			e.printStackTrace();
		} return res;
	}

}
