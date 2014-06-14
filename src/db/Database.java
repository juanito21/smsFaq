package db;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.transform.Result;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.Statement;

public class Database {

	private static String DB_NAME = "smsfaq";
	private static String PASSWORD = "";
	private static String USER = "root";
	private static String SGBD = "mysql";
	private static String SERVER_NAME = "localhost";
	private Connection con;
	
	public Database() throws SQLException {
		loadDriver();
		System.out.println("Connection etablishing...");
		con = (Connection) DriverManager.getConnection("jdbc:" +
														SGBD +
														"://" +
														SERVER_NAME +
														"/" +
														DB_NAME, USER, PASSWORD);
		System.out.println("Connection etablished !");
	}
	
	private void loadDriver() {
		System.out.println("Loading driver...");
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Driver Loaded !");
	}
	
	public Connection getCon() {
		return con;
	}
	
	public void disconnect() {
		
	}
	
	public int execUpdate(String sqlQuery) throws SQLException {
	      Statement state = (Statement) con.createStatement();
	      int res = state.executeUpdate(sqlQuery);
	      state.close();
	      return res;
	}
	
	public ResultSetMetaData execQuery(String sqlQuery) throws SQLException {
	      Statement state = (Statement) con.createStatement();
	      ResultSet res = (ResultSet) state.executeQuery(sqlQuery);
	      state.close();
	      return (ResultSetMetaData) res.getMetaData();
	}
}
