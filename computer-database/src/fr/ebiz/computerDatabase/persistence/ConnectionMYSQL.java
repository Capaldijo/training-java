package fr.ebiz.computerDatabase.persistence;


import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import fr.ebiz.computerDatabase.utils.Utils;

public final class ConnectionMYSQL {
	
	// L'utilisation du mot clé volatile, en Java 5 et sup,
    // permet d'éviter le cas où "Singleton.instance" est non-null,
    // mais pas encore "réellement" instancié.
    private static volatile ConnectionMYSQL instance = null;
	
	private Connection connexion = null;
	
	private ConnectionMYSQL() {	
		super();
		
		try {
			/* Loading driver JDBC for MySQL */
			Class.forName( "com.mysql.jdbc.Driver" );
			
			/* 
			 * setting properties for the connection
			 * useSSL set to false or else it does not connect
			 * zeroDateTimeBehavior to avoid error with null date
			 */
			Properties props = new Properties();
			props.setProperty("user", Utils.userDB);
			props.setProperty("password", Utils.passwdDB);
			props.setProperty("useSSL","false");
			props.setProperty("zeroDateTimeBehavior","convertToNull");
			
			// Connection to Database
		    connexion = (Connection) DriverManager.getConnection(Utils.urlDB, props);

		}catch ( ClassNotFoundException e ) {
			System.out.println("Error loading JDBC Driver");
		} catch ( SQLException e ) {
		    System.out.println("Error connecting to DB");
		}
	}
	
	public final static ConnectionMYSQL getInstance() {
        //Le "Double-Checked Singleton"/"Singleton doublement vérifié" permet 
        //d'éviter un appel coûteux à synchronized, 
        //une fois que l'instanciation est faite.
        if (ConnectionMYSQL.instance == null) {
           // Le mot-clé synchronized sur ce bloc empêche toute instanciation
           // multiple même par différents "threads".
           synchronized(ConnectionMYSQL.class) {
             if (ConnectionMYSQL.instance == null) {
            	 ConnectionMYSQL.instance = new ConnectionMYSQL();
             }
           }
        }
        return ConnectionMYSQL.instance;
    }
	
	
	public Connection getConnection() {
		return connexion;
	}
	
	public void closeAll() throws SQLException {
        connexion.close();
	}
}
