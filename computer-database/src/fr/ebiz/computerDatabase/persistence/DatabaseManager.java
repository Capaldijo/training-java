package fr.ebiz.computerDatabase.persistence;


import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import fr.ebiz.computerDatabase.utils.Utils;

public final class DatabaseManager {
	
	// L'utilisation du mot clé volatile, en Java 5 et sup,
    // permet d'éviter le cas où "Singleton.instance" est non-null,
    // mais pas encore "réellement" instancié.
    private static volatile DatabaseManager instance = null;
	
	private Statement statement = null;
	
	private Connection connexion = null;
	
	private DatabaseManager() {	
		super();
		
		try {
			/* Chargement du driver JDBC pour MySQL */
			Class.forName( "com.mysql.jdbc.Driver" );
			
			Properties props = new Properties();
			props.setProperty("user", Utils.userDB);
			props.setProperty("password", Utils.passwdDB);
			props.setProperty("useSSL","false");
			props.setProperty("zeroDateTimeBehavior","convertToNull");
			
			// Connection to Database
		    connexion = (Connection) DriverManager.getConnection(Utils.urlDB, props);
		    
		    // Object handling requests
		    statement = (Statement) connexion.createStatement();

		}catch ( ClassNotFoundException e ) {
			System.out.println("Error loading JDBC Driver");
		} catch ( SQLException e ) {
		    System.out.println("Error connecting to DB");
		}
	}
	
	public final static DatabaseManager getInstance() {
        //Le "Double-Checked Singleton"/"Singleton doublement vérifié" permet 
        //d'éviter un appel coûteux à synchronized, 
        //une fois que l'instanciation est faite.
        if (DatabaseManager.instance == null) {
           // Le mot-clé synchronized sur ce bloc empêche toute instanciation
           // multiple même par différents "threads".
           synchronized(DatabaseManager.class) {
             if (DatabaseManager.instance == null) {
            	 DatabaseManager.instance = new DatabaseManager();
             }
           }
        }
        return DatabaseManager.instance;
    }
	
	
	public ResultSet execQuery(String query) throws SQLException {
	    return statement.executeQuery(query);
	}
	
	public ResultSet execQueryPageable(String query, int numPage, int nbLine) throws SQLException {
		PreparedStatement prepStatement = (PreparedStatement) connexion.prepareStatement(query);
		prepStatement.setInt(1, numPage);
		prepStatement.setInt(2, nbLine);
		return prepStatement.executeQuery();
	}
	
	/* ----- COMPANY REQUEST PART ----- */
	
	public ResultSet getCompanyById(String query, int id) throws SQLException {
		PreparedStatement prepStatement = (PreparedStatement) connexion.prepareStatement(query);
		prepStatement.setInt(1, id);
		return prepStatement.executeQuery();
	}
	
	
	/* ----- COMPUTER REQUEST PART ----- */
	
	public ResultSet getComputerById(String query, int id) throws SQLException {
		PreparedStatement prepStatement = (PreparedStatement) connexion.prepareStatement(query);
		prepStatement.setInt(1, id);
		return prepStatement.executeQuery();
	}
	
	public int insertComputer(String query, String name, String intro, String discon, int compIdRef) throws SQLException {
		PreparedStatement prepStatement = (PreparedStatement) connexion.prepareStatement(query);
		prepStatement.setString(1, name);
		prepStatement.setString(2, intro);
		prepStatement.setString(3, discon);
		prepStatement.setInt(4, compIdRef);
		return prepStatement.executeUpdate();
	}
	
	public int updateComputer(String query, int id, String name, String intro, String discon, int compIdRef) throws SQLException {
		PreparedStatement prepStatement = (PreparedStatement) connexion.prepareStatement(query);
		prepStatement.setString(1, name);
		prepStatement.setString(2, intro);
		prepStatement.setString(3, discon);
		prepStatement.setInt(4, compIdRef);
		prepStatement.setInt(5, id);
		return prepStatement.executeUpdate();
	}
	
	public int deleteComputer(String query, int id) throws SQLException {
		PreparedStatement prepStatement = (PreparedStatement) connexion.prepareStatement(query);
		prepStatement.setInt(1, id);
		return prepStatement.executeUpdate();
	}
	
	// Close first 
	public void closeAll() throws SQLException {
		statement.close();
        connexion.close();
	}
}
