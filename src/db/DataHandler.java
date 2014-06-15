package db;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class DataHandler {
	
	private Database db;
	private PreparedStatement preStmnt;
	private ResultSet rs;
	private static String TAG = "DataHandler";
	
	public DataHandler(Database myDB) throws SQLException {
		db = myDB;
	}
	
	public void start() {
		
	}
	
	public void close() {
			try {
				if(preStmnt!=null) preStmnt.close();
				if(rs!=null) rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	public void commit() {
		try {
			db.getConnection().commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void truncateDB() throws SQLException {
		String sql = "TRUNCATE smsFaq.*;";
		preStmnt = (PreparedStatement) db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		db.execUpdate(preStmnt);
	}
	
	public int addQuestion(String q) throws SQLException {
		String sql = "INSERT INTO questions (question) values (?);";
		preStmnt = (PreparedStatement) db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		preStmnt.setString(1,q);
		int id = db.execUpdate(preStmnt);
		return id;
	}
	
	public int addAnswer(String a) throws SQLException {
		String sql = "INSERT INTO answers (answer) values (?);";
		preStmnt = (PreparedStatement) db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		preStmnt.setString(1,a);
		int id = db.execUpdate(preStmnt);
		return id;
	}
	
	public int addTerm(String t) throws SQLException {
		String sql = "INSERT INTO terms (term) values (?);";
		preStmnt = (PreparedStatement) db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		preStmnt.setString(1,t);
		int id = db.execUpdate(preStmnt);
		return id;
	}
	
	public int addQuestionTerm(int t, int q) throws SQLException {
		String sql = "INSERT INTO questionsterms (idTerm,idQuestion) values (?,?);";
		preStmnt = (PreparedStatement) db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		preStmnt.setInt(1,t);
		preStmnt.setInt(2,q);
		int id = db.execUpdate(preStmnt);
		return id;
	}
	
	public int addQuestionAnwser(int q, int a) throws SQLException {
		String sql = "INSERT INTO questionsanswers (idQuestion,idAnswer) values (?,?);";
		preStmnt = (PreparedStatement) db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		preStmnt.setInt(1,q);
		preStmnt.setInt(2,a);
		int id = db.execUpdate(preStmnt);
		return id;
	}
	
	public boolean isTermExists(String t) throws SQLException {
		String sql = "SELECT * FROM terms WHERE term=?;";
		preStmnt = (PreparedStatement) db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		preStmnt.setString(1,t);
		rs = db.execQuery(preStmnt);
		boolean b = rs.next();
		rs.close();
		return b;
	}
	
	public boolean isQuestionExists(String q) throws SQLException {
		String sql = "SELECT * FROM questions WHERE question=?;";
		preStmnt = (PreparedStatement) db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		preStmnt.setString(1,q);
		rs = db.execQuery(preStmnt);
		boolean b = rs.next();
		rs.close();
		return b;
	}
	
	public boolean isAnswerExists(String a) throws SQLException {
		String sql = "SELECT * FROM answers WHERE answer=?;";
		preStmnt = (PreparedStatement) db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		preStmnt.setString(1,a);
		rs = db.execQuery(preStmnt);
		boolean b = rs.next();
		rs.close();
		return b;
	}

}
