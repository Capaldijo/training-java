package fr.ebiz.computerDatabase.persistence;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.mysql.jdbc.Connection;
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
           // Il est TRES important.
           synchronized(DatabaseManager.class) {
             if (DatabaseManager.instance == null) {
            	 DatabaseManager.instance = new DatabaseManager();
             }
           }
        }
        return DatabaseManager.instance;
    }
	
	public ResultSet getCompanies() throws SQLException {
	    return statement.executeQuery("SELECT id, name FROM company;");
	}
	
	public void executeQuery(String query) throws SQLException {
		statement.executeQuery(query);
	}
	
	public void closeAll() throws SQLException {
		statement.close();
        connexion.close();
	}
}
