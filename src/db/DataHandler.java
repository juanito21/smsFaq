package db;

import java.sql.SQLException;

public class DataHandler {
	
	private Database db;
	
	public DataHandler() throws SQLException {
		db = new Database();
	}
	
	public void addQuestion(String q) throws SQLException {
		String sql = "INSERT INTO questions (question) values ('" + q + "');";
		db.execUpdate(sql);
	}

}
