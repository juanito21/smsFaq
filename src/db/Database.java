package db;

import static functions.CommonFunctions.log;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class Database {

	private static String DB_NAME = "smsfaq";
	private static String PASSWORD = "";
	private static String USER = "root";
	private static String SGBD = "mysql";
	private static String SERVER_NAME = "localhost";
	private static String TAG = "Database";
	private static Database instance;
	private Connection con;
	public static int count = 0;
	
	private Database() {
		loadDriver();
		con = createConnection();
	}
	
	private Connection createConnection() {
		Connection con = null;
		try {
			log(TAG, "Connection etablishing...");
			con = (Connection) DriverManager.getConnection("jdbc:" +
															SGBD +
															"://" +
															SERVER_NAME +
															"/" +
															DB_NAME, USER, PASSWORD);
			log(TAG, "Connection etablished !");
		} catch(SQLException e) {
			e.printStackTrace();
		} return con;
	}
	
	private void loadDriver() {
		log(TAG, "Loading driver...");
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log(TAG, "Driver Loaded !");
	}
	
	public static synchronized Database getInstance() {
		if(instance == null) instance = new Database();
		return instance;
	}
	
	public Connection getConnection() {
		return instance.con;
	}
	
	public void disconnect() {
		try {
			log(TAG, "Database disconnecting...");
			getConnection().close();
			log(TAG, "Database disconnected");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log(TAG, count + " requests executed during this session");
		instance = null;
		count = 0;
	}
	
	public int execUpdate(PreparedStatement preStmnt) throws SQLException {
		int id = -1;
		preStmnt.executeUpdate();
		ResultSet rs = preStmnt.getGeneratedKeys();
		if(rs.next()) id = rs.getInt(1);
		rs.close();
		count++;
		return id;
	}
	
	public ResultSet execQuery(PreparedStatement preStmnt) throws SQLException {
		count++;
		return (ResultSet) preStmnt.executeQuery();
	}
}
