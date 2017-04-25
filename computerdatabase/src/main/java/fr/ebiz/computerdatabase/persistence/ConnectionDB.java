package fr.ebiz.computerdatabase.persistence;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.mysql.jdbc.Connection;

import fr.ebiz.computerdatabase.exceptions.ConnectionException;
import fr.ebiz.computerdatabase.utils.Utils;

public final class ConnectionDB {

    // L'utilisation du mot clé volatile, en Java 5 et sup,
    // permet d'éviter le cas où "Singleton.instance" est non-null,
    // mais pas encore "réellement" instancié.
    private static volatile ConnectionDB instance = null;

    private Connection connexion = null;

    /**
     * Contructor for connection to mysql db.
     * @throws ConnectionException if error on co to db
     */
    private ConnectionDB() throws ConnectionException {
        super();

        try {
            /* Loading driver JDBC for MySQL */
            Class.forName("com.mysql.jdbc.Driver");

            /*
             * setting properties for the connection useSSL set to false or else
             * it does not connect zeroDateTimeBehavior to avoid error with null
             * date
             */
            Properties props = new Properties();
            props.setProperty("user", Utils.USERDB);
            props.setProperty("password", Utils.PASSWDDB);
            props.setProperty("useSSL", "false");
            props.setProperty("zeroDateTimeBehavior", "convertToNull");

            // Connection to Database
            connexion = (Connection) DriverManager.getConnection(Utils.URLDB, props);

        } catch (ClassNotFoundException e) {
            throw new ConnectionException("[DRIVER] Error loading JDBC Driver");
        } catch (SQLException e) {
            throw new ConnectionException("[DB] Error connecting to DB");
        }
    }

    /**
     * Get instance of co to db.
     * @return connectionMysql instance
     * @throws ConnectionException if error on co to db
     */
    public static ConnectionDB getInstance() throws ConnectionException {
        // Le "Double-Checked Singleton"/"Singleton doublement vérifié" permet
        // d'éviter un appel coûteux à synchronized,
        // une fois que l'instanciation est faite.
        if (ConnectionDB.instance == null) {
            // Le mot-clé synchronized sur ce bloc empêche toute instanciation
            // multiple même par différents "threads".
            synchronized (ConnectionDB.class) {
                if (ConnectionDB.instance == null) {
                    ConnectionDB.instance = new ConnectionDB();
                }
            }
        }
        return ConnectionDB.instance;
    }

    public Connection getConnection() {
        return connexion;
    }

    /**
     * @throws ConnectionException if error to co to db
     */
    public void closeAll() throws ConnectionException {
        try {
            connexion.close();
        } catch (SQLException e) {
            throw new ConnectionException("[CLOSING] Error closing DB");
        }
    }
}
