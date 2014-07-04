package db;

import static functions.CommonFunctions.log;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

/**
 * Database manage connection with the MySQL server
 * @author Jean
 *
 */
public class Database {

	private static String DB_NAME = "smsfaq_dev";
	private static String PASSWORD = "";
	private static String USER = "root";
	private static String SGBD = "mysql";
	private static String SERVER_NAME = "localhost";
	private static String TAG = "Database";
	
	protected static String T_QUESTIONS = "questions";
	protected static String Q_ID= "id";
	protected static String Q_QUESTION = "question";
	
	protected static String T_ANSWERS = "answers";
	protected static String A_ID= "id";
	protected static String A_ANSWER = "answer";
	
	protected static String T_TERMS = "terms";
	protected static String T_ID= "id";
	protected static String T_TERM = "term";
	
	protected static String T_DICT = "dictionaryTerms";
	protected static String D_ID = "id";
	protected static String D_TERM = "term";
	
	protected static String T_QUESTIONSTERMS = "questionsterms";
	protected static String QT_IDQUESTION = "idQuestion";
	protected static String QT_IDTERM = "idTerm";
	
	protected static String T_QUESTIONSANSWERS = "questionsAnswers";
	protected static String QA_IDQUESTION = "idQuestion";
	protected static String QA_IDANSWER = "idAnswer";
	
	protected static String T_STOPWORDS = "stopwords";
	protected static String SW_ID = "id";
	protected static String SW_WORD = "word";
	
	private static Database instance;
	private Connection con;
	public static int count = 0;
	
	/**
	 * Constructor
	 */
	private Database() {
		loadDriver();
		con = createConnection();
	}
	
	/**
	 * Create a connection between JDBC and MySQL
	 * @return the connection object
	 */
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
			log(TAG, "An error was detected while connecting to the database.");
			e.printStackTrace();
		} return con;
	}
	
	/**
	 * Load the MySQL driver
	 */
	private void loadDriver() {
		log(TAG, "Loading driver...");
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			log(TAG, "An error cas detected while loading driver");
			e.printStackTrace();
		}
		log(TAG, "Driver Loaded !");
	}
	
	/**
	 * Get the current instance class. Here we use the singleton pattern.
	 * @return the database object
	 */
	public static synchronized Database getInstance() {
		if(instance == null) instance = new Database();
		return instance;
	}
	
	/**
	 * Get the connection of the current instance
	 * @return the connection object
	 */
	public Connection getConnection() {
		return instance.con;
	}
	
	/**
	 * Disconnect the database
	 */
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
	
	/**
	 * Execute an update query (DELETE, INSERT, UPDATE,..)
	 * @param preStmnt : the PreparedStatement object
	 * @return -1 if there is an error, the id of the row inserted else.
	 * @throws SQLException
	 */
	public int execUpdate(PreparedStatement preStmnt) throws SQLException {
		int id = -1;
		preStmnt.executeUpdate();
		ResultSet rs = preStmnt.getGeneratedKeys();
		if(rs.next()) id = rs.getInt(1);
		rs.close();
		count++;
		return id;
	}
	
	/**
	 * Execute a query (SELECT)
	 * @param preStmnt : PreparedStatement object
	 * @return a ResultSet object
	 * @throws SQLException
	 */
	public ResultSet execQuery(PreparedStatement preStmnt) throws SQLException {
		count++;
		return (ResultSet) preStmnt.executeQuery();
	}
}
