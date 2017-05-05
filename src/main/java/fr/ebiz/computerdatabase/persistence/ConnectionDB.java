package fr.ebiz.computerdatabase.persistence;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import fr.ebiz.computerdatabase.exceptions.ConnectionException;

public final class ConnectionDB {

    private static ConnectionDB instance = new ConnectionDB();

    static final Logger LOG = LoggerFactory.getLogger(ConnectionDB.class);

    private Connection connexion = null;

    private HikariDataSource ds = null;

    /**
     * Contructor for connection to mysql db.
     * @throws ConnectionException if error on co to db
     */
    private ConnectionDB() {
        super();

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            LOG.error("[CONNECTION] error on loading driver jdbc.");
            throw new RuntimeException("[CONNECTION] error on loading driver jdbc.");
        }
        HikariConfig cfg = new HikariConfig("/hikari.properties");
        ds = new HikariDataSource(cfg);
    }

    /**
     * Get instance of co to db.
     * @return connectionMysql instance
     * @throws ConnectionException if error on co to db
     */
    public static ConnectionDB getInstance() throws ConnectionException {
        if (ConnectionDB.instance == null) {
            ConnectionDB.instance = new ConnectionDB();
        }
        return ConnectionDB.instance;
    }

    /**
     * Get connection.
     * @return Connection with hikari
     * @throws ConnectionException Error on co to db
     */
    public Connection getConnection() throws ConnectionException {
        try {
            connexion = ds.getConnection();
        } catch (SQLException e) {
            LOG.error("[CONNECTION] Error connection to DB.");
            throw new ConnectionException("[CONNECTION] Error connection to DB.");
        }
        return connexion;
    }

    /**
     * @throws ConnectionException if error to co to db
     */
    public void closeAll() throws ConnectionException {
        try {
            connexion.close();
            ds.close();
        } catch (SQLException e) {
            LOG.error("[CLOSING] Error closing DB.");
            throw new ConnectionException("[CLOSING] Error closing DB");
        }
    }
}
