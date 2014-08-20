package db;

import static functions.CommonFunctions.log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

/**
 * Handle data from database
 * @author Jean
 * @param
 *
 */
public class DataHandler {
	
	protected Database db;
	protected PreparedStatement preStmnt;
	protected ResultSet rs;
	private static String TAG = "DataHandler";
	
	/**
	 * The constructor
	 * @param myDB : a database object is required. This param determine the database to handle.
	 */
	public DataHandler(Database myDB) {
		db = myDB;
	}
	
	/**
	 * Close the database handler
	 */
	public void close() {
			try {
				if(preStmnt!=null) preStmnt.close();
				if(rs!=null) rs.close();
			} catch (SQLException e) {
				log(TAG, "An error was occured during connection closing.");
				e.printStackTrace();
			}
	}
	
	/**
	 * Get the String formated for SQL query field
	 * @param data
	 * @return the String formated
	 */
	public <V> String getFieldUpdateFormat(LinkedHashMap<String, V> data) {
		String res = "";
		for(Entry<String, V> e : data.entrySet()) res += e.getKey() + ",";
		return res.substring(0, res.length()-1);
	}
	
	/**
	 * Get the String formated for SQL query value
	 * @param n
	 * @return the String formated
	 */
	public String getDataValueUpdateFormat(int n) {
		String res = "";
		for(int i=0;i<n;i++) {
			res += "?";
			if(i<n-1) res+=",";
		} return res;
	}
	
	/**
	 * Get the value of recording data
	 * @param data
	 * @return the String value of data
	 */
	public <V> String getDataValue(LinkedHashMap<String, V> data) {
		String res = "(";
		int i = 0;
		for(Entry<String, V> e : data.entrySet()) {
			res += e.getValue();
			if(i<data.size()-1) res += ",";
			res += ")";
		} return res;
	}
	
	/**
	 * Add something in the database
	 * @param tableName : the table name
	 * @param data : the data to save
	 * @param msg : message in case of an error is detected
	 * @return the insert id or -1 in case of an error is detected
	 */
	public <V> int addSomething(String tableName, LinkedHashMap<String, V> data, String msg) {
		String sql = "INSERT INTO " + tableName + "(" + getFieldUpdateFormat(data) + ") values (" + getDataValueUpdateFormat(data.size()) + ");"; 
		log(TAG, sql);
		int id = -1;
		int i = 1;
		try {
			preStmnt = (PreparedStatement) db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			for(Entry<String, V> e : data.entrySet()) {
				if(e.getValue() instanceof String) preStmnt.setString(i,(String) e.getValue());
				else if(e.getValue() instanceof Integer)preStmnt.setInt(i,(Integer) e.getValue());
				i++;
			}
			id = db.execUpdate(preStmnt);
		} catch (SQLException e) {
			log(TAG, "An error was detected while adding " + msg + " : " + getDataValue(data));
			e.printStackTrace();
		} return id;
	}
	
	/**
	 * Update something from the database
	 * @param tableName : the table name
	 * @param fieldCompare : the field to compare
	 * @param updateField : the field to update
	 * @param valueCompare : the value to compare
	 * @param newValue : the new value after the update
	 */
	public void updateSomething(String tableName, String fieldCompare, String updateField, String valueCompare, String newValue) {
		String sql = "UPDATE " + tableName + " SET " + updateField + " = ? WHERE " + fieldCompare + " = ?;";
		try {
			preStmnt = (PreparedStatement) db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preStmnt.setDouble(1,Double.parseDouble(newValue));
			preStmnt.setString(2,valueCompare);
			db.execUpdate(preStmnt);
		} catch (SQLException e) {
			log(TAG, "An error was detected while updating a " + tableName + " : " + valueCompare);
			e.printStackTrace();
		}
	}
	
	/**
	 * Check if something exists in the database
	 * @param tableName : the table name
	 * @param field : the field to check
	 * @param value : the value to compare
	 * @param msg : the message in case of an error is detected
	 * @return TRUE if field(value) exists, FALSE else
	 */
	public boolean isSomethingExists(String tableName, String field, String value, String msg) {
		String sql = "SELECT * FROM " + tableName + " WHERE " + field + "=?;";
		boolean b = false;
		try {
			preStmnt = (PreparedStatement) db.getConnection().prepareStatement(sql);
			preStmnt.setString(1,value);
			rs = db.execQuery(preStmnt);
			b = rs.next();
			rs.close();
		} catch (SQLException e) {
			log(TAG, "An error was detected while verifying if a " + msg + " exists : " + value);
			e.printStackTrace();
		} return b;
	}

	/**
	 * Get something from the database
	 * @param tableName
	 * @param fieldGet
	 * @param fieldCompare
	 * @param value
	 * @return the first line returned from the database
	 */
	public int getSomething(String tableName, String fieldGet, String fieldCompare, String value) {
		String sql = "SELECT (" + fieldGet  + ") FROM " + tableName + " WHERE " + fieldCompare + " = ?;";
		int res = -1;
		try {
			preStmnt = (PreparedStatement) db.getConnection().prepareStatement(sql);
			preStmnt.setString(1,value);
			rs = db.execQuery(preStmnt);
			if(rs.next()) res = rs.getInt(fieldGet);
			rs.close();
		} catch (SQLException e) {
			log(TAG, "An error was detected while getting a " + tableName + " : " + value);
			e.printStackTrace();
		} return res;
		
	}
	
	/**
	 * Get all line from a table in the database
	 * @param tableName : the name of the table
	 * @param fn1 : the first field of returned tuple
	 * @param fn2 : the second field of returned tuple
	 * @return the list of tuple
	 */
	public List<Map.Entry<Integer, String>> getAllSomething(String tableName, String fn1, String fn2) {
		String sql = "SELECT * FROM " + tableName + ";";
		List<Map.Entry<Integer, String>> L = new ArrayList<Map.Entry<Integer, String>>();
		try {
			preStmnt = (PreparedStatement) db.getConnection().prepareStatement(sql);
			rs = db.execQuery(preStmnt);
			AbstractMap.SimpleEntry<Integer, String> tuple;
			while(rs.next()) {
				tuple = new AbstractMap.SimpleEntry<Integer, String>(rs.getInt(fn1), rs.getString(fn2));
				L.add(tuple);
			}
			rs.close();
		} catch (SQLException e) {
			log(TAG, "An error was detected while getting " + tableName + " list");
			e.printStackTrace();
		} return L;
	}
	
	/**
	 * Get all line from table in the database
	 * @param tableName : the table name
	 * @param fieldGet : the field to get
	 * @return the list of single tuple
	 */
	public List<String> getAllSomething(String tableName, String fieldGet) {
		String sql = "SELECT * FROM " + tableName + ";";
		List<String> L = new ArrayList<String>();
		try {
			preStmnt = (PreparedStatement) db.getConnection().prepareStatement(sql);
			rs = db.execQuery(preStmnt);
			String s;
			while(rs.next()) {
				s = rs.getString(fieldGet);
				L.add(s);
			}
			rs.close();
		} catch (SQLException e) {
			log(TAG, "An error was detected while getting " + tableName + " list");
			e.printStackTrace();
		} return L;
	}
}
